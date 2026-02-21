package com.example.data.database.entity  // adjust package

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_cache")
data class LocationEntity(
    @PrimaryKey val id: Int = 1,  // only one row
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)