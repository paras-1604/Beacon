package com.example.data.repository

import android.util.Log
import com.example.domain.model.Alert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlertRepository(
    private val supabaseClient: SupabaseClient
) {
    suspend fun sendAlert(alert: Alert): Result<Unit> = withContext(Dispatchers.IO) {
        Log.d("AlertRepo", "📤 Attempting to send alert: $alert")
        try {
            val alertMap = mapOf(
                "user_id" to alert.user_id,
                "latitude" to alert.latitude,
                "longitude" to alert.longitude,
                "severity" to alert.severity,
                "is_offline" to alert.is_offline
            )
            Log.d("AlertRepo", "Insert map: $alertMap")
            supabaseClient.postgrest["alerts"].insert(alertMap)
            Log.d("AlertRepo", "✅ Insert successful")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AlertRepo", "❌ Insert failed", e)
            Result.failure(e)
        }
    }
}