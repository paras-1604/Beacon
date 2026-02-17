package com.example.data

import android.util.Log
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {


    suspend fun signInAnonymously(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // 1. Perform anonymous sign-in (returns Unit or a status object, not the user)
            SupabaseClient.client.auth.signInAnonymously()

            // 2. Retrieve the user from the auth session now that sign-in is complete
            val user = SupabaseClient.client.auth.currentUserOrNull()
            val userId = user?.id ?: "unknown"

            Log.d("SupabaseAuth", "✅ Anonymous sign-in successful! User ID: $userId")

            Result.success(userId)
        } catch (e: AuthRestException) {
            Log.e("SupabaseAuth", "❌ Auth failed: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("SupabaseAuth", "❌ Unexpected error: ${e.message}")
            Result.failure(e)
        }
    }
}