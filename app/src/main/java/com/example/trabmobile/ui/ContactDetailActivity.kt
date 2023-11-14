package com.example.trabmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.trabmobile.R
import com.example.trabmobile.database.DBHelper
import com.example.trabmobile.databinding.ActivityContactDetailBinding
import com.example.trabmobile.model.ContactModel

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db: DBHelper
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int = -1
    private var contactModel = ContactModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val id = i.extras?.getInt("id")

        db = DBHelper(applicationContext)

        if (id != null) {
            contactModel = db.getContact(id)
            populate()
        } else {
            finish()
        }

        binding.buttonBack.setOnClickListener {
            setResult(0, i)
            finish()
        }

        binding.buttonEdit.setOnClickListener {
            binding.layoutEditDelete.visibility = View.VISIBLE
            binding.layoutEdit.visibility = View.GONE
            changeEditText(true)
        }

        binding.buttonCancel.setOnClickListener {
            binding.layoutEditDelete.visibility = View.GONE
            binding.layoutEdit.visibility = View.VISIBLE
            populate()
            changeEditText(false)
        }

        binding.buttonSave.setOnClickListener {
            val res = db.updateContact(
                id = contactModel.id,
                name = binding.editName.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editPhone.text.toString().toInt(),
                imageId = imageId)

            if (res > 0) {
                Toast.makeText(applicationContext, "Contato salvo", Toast.LENGTH_SHORT).show()
                setResult(1, i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Update ERROR", Toast.LENGTH_SHORT).show()
                setResult(0, i)
                finish()
            }
        }

        binding.buttonDelete.setOnClickListener {
            val res = db.deleteContact(contactModel.id)

            if (res > 0) {
                Toast.makeText(applicationContext, "Contato excluído", Toast.LENGTH_SHORT).show()
                setResult(1, i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Delete ERROR", Toast.LENGTH_SHORT).show()
                setResult(0, i)
                finish()
            }
        }

        binding.imageContact.setOnClickListener{
            if (binding.editEmail.isEnabled) {

                launcher.launch(
                    Intent(
                        applicationContext,
                        ContactImageSelectionActivity::class.java
                    )
                )
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                if (it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("id", 0)!!
                    binding.imageContact.setImageResource(imageId)
                }
            } else {
                imageId = -1
                binding.imageContact.setImageResource(R.drawable.profiledefault)
            }
        }

    }

    private fun changeEditText(status: Boolean) {
        binding.editName.isEnabled = status
        binding.editEmail.isEnabled = status
        binding.editPhone.isEnabled = status
    }

    private fun populate() {
        binding.editName.setText((contactModel.name))
        binding.editEmail.setText((contactModel.email))
        binding.editPhone.setText((contactModel.phone.toString()))

        if (contactModel.imageId > 0) {
            binding.imageContact.setImageResource(contactModel.imageId)
        } else {
            binding.imageContact.setImageResource(R.drawable.profiledefault)
        }
    }
}