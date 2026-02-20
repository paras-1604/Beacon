package com.example.presentation.screens.contacts



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Contact
import com.example.domain.repository.ContactRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private val _isAddingContact = MutableStateFlow(false)
    val isAddingContact: StateFlow<Boolean> = _isAddingContact.asStateFlow()

    private val _showDeleteConfirmation = MutableStateFlow<Contact?>(null)
    val showDeleteConfirmation: StateFlow<Contact?> = _showDeleteConfirmation.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.getAllContacts().collect { contactList ->
                _contacts.value = contactList
            }
        }
    }

    fun showAddContactDialog() {
        _isAddingContact.value = true
    }

    fun hideAddContactDialog() {
        _isAddingContact.value = false
    }

    fun addContact(name: String, phoneNumber: String, email: String?) {
        viewModelScope.launch {
            contactRepository.addContact(name, phoneNumber, email)
            hideAddContactDialog()
        }
    }

    fun confirmDeleteContact(contact: Contact) {
        _showDeleteConfirmation.value = contact
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact)
            _showDeleteConfirmation.value = null
        }
    }

    fun cancelDelete() {
        _showDeleteConfirmation.value = null
    }
}