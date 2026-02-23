package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val user_id: String,
    val latitude: Double?,
    val longitude: Double?,
    val severity: String,
    val is_offline: Boolean = false
)