package com.example.data.database.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "received_alerts")
data class ReceivedAlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val latitude: Double?,
    val longitude: Double?,
    val severity: String,
    val timestamp: String?,
    val isRead: Boolean = false  // optional – can be used later to mark unread
)