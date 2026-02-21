package com.example.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)