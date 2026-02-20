package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.ContactDao
import com.example.data.database.entity.ContactEntity


@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                // We'll initialize this from the application context later
                throw IllegalStateException("Database not initialized. Call initialize() first.")
            }
        }

        fun initialize(database: ContactDatabase) {
            INSTANCE = database
        }
    }
}