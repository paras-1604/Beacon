package com.example.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location as AndroidLocation  // alias to avoid name clash
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.example.data.database.ContactDatabase
import com.example.data.database.entity.LocationEntity
import com.example.domain.model.Location
import com.example.domain.repository.LocationRepository
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

import kotlinx.coroutines.flow.firstOrNull

class LocationRepositoryImpl(
    private val context: Context,
    private val database: ContactDatabase
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> = callbackFlow @androidx.annotation.RequiresPermission(
        anyOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]
    ) {
        // Check permission before starting
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            close(Exception("Location permission not granted"))
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000  // update every 5 seconds
        ).setMinUpdateIntervalMillis(2000)  // at most every 2 seconds
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    trySend(location.toDomain())
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener { exception ->
            close(exception)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }.map { location ->
        // Save to database whenever a new location arrives
        saveToDb(location)
        location
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLastKnownLocation(): Location? {
        // Try to get from FusedLocationProvider first (fastest)
        val freshLocation = try {
            fusedLocationClient.lastLocation.await()?.toDomain()
        } catch (e: SecurityException) {
            null
        }
        if (freshLocation != null) return freshLocation

        // Fallback to database
//        val entity = database.locationDao().getLocation().map { it }.firstOrNull()



        val entity = database.locationDao().getLocation().firstOrNull()
        return entity?.toDomain()
    }

    private suspend fun saveToDb(location: Location) {
        val entity = LocationEntity(
            id = 1,
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            timestamp = location.timestamp
        )
        database.locationDao().insertLocation(entity)
    }
}

// Extension functions to convert between Android Location and domain model
fun AndroidLocation.toDomain(): Location = Location(
    latitude = latitude,
    longitude = longitude,
    accuracy = accuracy,
    timestamp = time
)

fun LocationEntity.toDomain(): Location = Location(
    latitude = latitude,
    longitude = longitude,
    accuracy = accuracy,
    timestamp = timestamp
)