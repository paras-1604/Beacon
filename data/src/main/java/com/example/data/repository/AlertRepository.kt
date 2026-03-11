package com.example.data.repository

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.data.database.ContactDatabase
import com.example.data.database.entity.ReceivedAlertEntity
import com.example.domain.model.Alert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertRepository(
    private val supabaseClient: SupabaseClient,
    private val database: ContactDatabase,
    private val context: Context
) {
    suspend fun sendAlert(alert: Alert): Result<Unit> = withContext(Dispatchers.IO) {
        Log.d("AlertRepo", "📤 Sending alert: $alert")
        try {
            supabaseClient.postgrest["alerts"].insert(alert)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AlertRepo", "❌ Insert failed", e)
            Result.failure(e)
        }
    }



    suspend fun deleteAlert(id: Long) {
        database.receivedAlertDao().deleteAlert(id)
    }

    suspend fun clearAllAlerts() {
        database.receivedAlertDao().clearAll()
    }

    /**
     * Returns a Flow of all received alerts from the local database.
     */
    fun getReceivedAlerts(): Flow<List<Alert>> {
        return database.receivedAlertDao().getAllAlerts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Observes new alerts from Supabase Realtime.
     */
    fun observeNewAlerts(): Flow<Alert> = callbackFlow {
        val channel = supabaseClient.realtime.channel("alerts-channel")

        val changeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "alerts"
        }

        val collectorJob = changeFlow.onEach { action ->
            Log.d("Realtime", "🔔 Realtime action received: $action")
            try {
                val alert = action.decodeRecord<Alert>()
                Log.d("Realtime", "✅ Decoded Alert: $alert")
                trySend(alert)

                // Insert into Room on a background thread
                CoroutineScope(Dispatchers.IO).launch {
                    val entity = ReceivedAlertEntity(
                        userId = alert.user_id,
                        latitude = alert.latitude,
                        longitude = alert.longitude,
                        severity = alert.severity,
                        timestamp = alert.timestamp
                    )
                    database.receivedAlertDao().insertAlert(entity)
                    Log.d("AlertRepository", "Inserted into Room: $alert")

                    // Show notification on main thread
                    withContext(Dispatchers.Main) {
                        showNotification(alert)
                    }
                }
            } catch (e: Exception) {
                Log.e("Realtime", "❌ Error decoding record", e)
            }
        }.launchIn(this)

        launch {
            try {
                supabaseClient.realtime.connect()
                channel.subscribe()
                Log.d("Realtime", "🚀 Subscribed to 'alerts-channel'")
            } catch (e: Exception) {
                Log.e("Realtime", "❌ Subscription failed", e)
                close(e)
            }
        }

        awaitClose {
            Log.d("Realtime", "🧹 Cleaning up Realtime channel")
            collectorJob.cancel()
            launch {
                try {
                    channel.unsubscribe()
                } catch (e: Exception) {
                    Log.e("Realtime", "Error during unsubscribe", e)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(alert: Alert) {
        val channelId = "alerts_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Emergency Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for emergency alerts"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        // Use getLaunchIntentForPackage to avoid direct dependency on MainActivity in the data module
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("🚨 ${alert.severity} Alert")
            .setContentText("From: ${alert.user_id.take(8)}... at ${alert.latitude}, ${alert.longitude}")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (hasNotificationPermission) {
            NotificationManagerCompat.from(context).notify(alert.hashCode(), notification)
        } else {
            Log.w("AlertRepository", "Permission POST_NOTIFICATIONS not granted")
        }
    }

    private fun ReceivedAlertEntity.toDomain(): Alert = Alert(
        id = id,
        user_id = userId,
        latitude = latitude,
        longitude = longitude,
        severity = severity,
        is_offline = false,
        timestamp = timestamp
    )
}
