package com.example.domain.repository



import com.example.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>
    suspend fun addContact(name: String, phoneNumber: String, email: String? = null)
    suspend fun deleteContact(contact: Contact)
}