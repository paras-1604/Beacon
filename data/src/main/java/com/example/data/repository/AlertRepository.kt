package com.example.data.repository

import android.util.Log
import com.example.domain.model.Alert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertRepository(
    private val supabaseClient: SupabaseClient
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

    /**
     * Observes new alerts from Supabase Realtime.
     * Note: postgresChangeFlow MUST be called BEFORE channel.subscribe().
     */
    fun observeNewAlerts(): Flow<Alert> = callbackFlow {
        val channel = supabaseClient.realtime.channel("alerts-channel")

        // 1. Define the change flow BEFORE subscribing.
        // This ensures the postgres_changes filter is sent with the join request.
        val changeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "alerts"
        }

        // 2. Collect changes and trySend them to the flow
        val collectorJob = changeFlow.onEach { action ->
            Log.d("Realtime", "🔔 Realtime action received: $action")
            try {
                val alert = action.decodeRecord<Alert>()
                Log.d("Realtime", "✅ Decoded Alert: $alert")
                trySend(alert)
            } catch (e: Exception) {
                Log.e("Realtime", "❌ Error decoding record", e)
            }
        }.launchIn(this)

        // 3. Connect and Subscribe
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

        // 4. Cleanup on flow cancellation
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
}
