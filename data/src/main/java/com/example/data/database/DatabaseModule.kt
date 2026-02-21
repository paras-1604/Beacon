package com.example.data.database


import android.content.Context
import androidx.room.Room

object DatabaseModule {
    fun provideDatabase(context: Context): ContactDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContactDatabase::class.java,
            "beacon_database"
        )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }
}