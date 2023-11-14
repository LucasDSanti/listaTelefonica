package com.example.trabmobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabmobile.R
import com.example.trabmobile.adapter.ContactListAdapter
import com.example.trabmobile.adapter.listener.ContactOnClickListener
import com.example.trabmobile.database.DBHelper
import com.example.trabmobile.databinding.ActivityMainBinding
import com.example.trabmobile.model.ContactModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactList: List<ContactModel>
    private lateinit var adapter: ContactListAdapter
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var dbHelper: DBHelper
    private var ascDesc: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(applicationContext)

        loadList()

        binding.buttonAdd.setOnClickListener {
            result.launch(Intent(applicationContext, NewContactActivity::class.java))
        }

        binding.buttonOrder.setOnClickListener {
            if (ascDesc) {
                binding.buttonOrder.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            } else {
                binding.buttonOrder.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            }

            ascDesc = !ascDesc
            contactList = contactList.reversed()
            placeAdapter()
        }

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                loadList()
            } else if (it.data != null && it.resultCode == 0){
                Toast.makeText(applicationContext, "Operação cancelada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun placeAdapter() {
        adapter = ContactListAdapter(contactList, ContactOnClickListener { contact ->
            val intent = Intent(applicationContext, ContactDetailActivity::class.java)
            intent.putExtra("id", contact.id)
            result.launch(intent)
        })
        binding.recyclerViewContacts.adapter = adapter
    }

    private fun loadList() {
        contactList = dbHelper.getAllContact().sortedWith(compareBy{it.name})
        placeAdapter()
    }
}