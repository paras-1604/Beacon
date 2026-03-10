package com.example.data


import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets

object SupabaseClient {
    // TODO: Move these to BuildConfig or secure storage later
    private const val SUPABASE_URL = "https://bhuprzzvbsumuxbavivs.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJodXByenp2YnN1bXV4YmF2aXZzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzA5NjMzMDMsImV4cCI6MjA4NjUzOTMwM30.g4I-pL9782f88JqBlbGKyCrbQZvjH5uWjx4dbCXqrFE"


    @OptIn(SupabaseInternal::class)
    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        httpEngine = OkHttp.create()
        
        httpConfig {
            install(WebSockets)
        }

        // Install the modules you need
        install(Postgrest)
        install(Auth)
        install(Realtime)
    }
}