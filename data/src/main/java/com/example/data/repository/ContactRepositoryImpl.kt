package com.example.data.repository

import com.example.data.database.ContactDatabase
import com.example.data.database.entity.ContactEntity
import com.example.domain.model.Contact
import com.example.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactRepositoryImpl(
    private val database: ContactDatabase
) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> {
        return database.contactDao().getAllContacts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addContact(name: String, phoneNumber: String, email: String?) {
        val entity = ContactEntity(
            name = name,
            phoneNumber = phoneNumber,
            email = email
        )
        database.contactDao().insertContact(entity)
    }

    override suspend fun deleteContact(contact: Contact) {
        val entity = ContactEntity(
            id = contact.id,
            name = contact.name,
            phoneNumber = contact.phoneNumber,
            email = contact.email
        )
        database.contactDao().deleteContact(entity)
    }
}

// Extension function to convert Entity to Domain model
fun ContactEntity.toDomain(): Contact {
    return Contact(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email
    )
}