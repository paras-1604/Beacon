package com.example.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.entity.ReceivedAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceivedAlertDao {
    @Query("SELECT * FROM received_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<ReceivedAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: ReceivedAlertEntity)

    @Query("DELETE FROM received_alerts WHERE id = :id")
    suspend fun deleteAlert(id: Long)
    @Query("DELETE FROM received_alerts")
    suspend fun clearAll()
}