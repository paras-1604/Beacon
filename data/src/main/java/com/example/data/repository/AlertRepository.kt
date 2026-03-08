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
            // Use type-safe insert with the @Serializable Alert class
            supabaseClient.postgrest["alerts"].insert(alert)
            Log.d("AlertRepo", "✅ Insert request completed")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AlertRepo", "❌ Insert failed", e)
            Result.failure(e)
        }
    }
}