package com.example.beacon


import android.app.Application
import com.example.data.database.ContactDatabase
import com.example.data.database.DatabaseModule


class BeaconApplication : Application() {

    // This will hold the database instance for the whole app
    lateinit var contactDatabase: ContactDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize Room database
        contactDatabase = DatabaseModule.provideDatabase(this)
        ContactDatabase.initialize(contactDatabase)

        // You can add other initializations here later
        // (like P2P manager when we implement it)
    }
}