package com.example.trabmobile.adapter.listener

import com.example.trabmobile.model.ContactModel

class ContactOnClickListener(val clickListener: (contact: ContactModel) -> Unit) {
    fun onClick(contact: ContactModel) = clickListener
}