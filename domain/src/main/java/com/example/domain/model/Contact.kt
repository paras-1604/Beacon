package com.example.domain.model

/**
 * Domain model representing a trusted contact.
 * This is a pure Kotlin class with no Android dependencies.
 */
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val email: String? = null
)