package com.example.trabmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trabmobile.R
import com.example.trabmobile.adapter.listener.ContactOnClickListener
import com.example.trabmobile.adapter.viewholder.ContactViewHolder
import com.example.trabmobile.model.ContactModel

class ContactListAdapter(
    private val contactList: List<ContactModel>,
    private val contactOnClickListener: ContactOnClickListener)
    : RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_contact, parent, false)
        return  ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.textName.text = contact.name

        if (contact.imageId > 0) {
            holder.image.setImageResource(contact.imageId)
        } else {
            holder.image.setImageResource((R.drawable.profiledefault))
        }

        holder.itemView.setOnClickListener {
            contactOnClickListener.clickListener(contact)
        }
    }
}