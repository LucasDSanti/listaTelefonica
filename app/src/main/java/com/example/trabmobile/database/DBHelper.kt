package com.example.trabmobile.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.trabmobile.model.ContactModel

class DBHelper(context: Context): SQLiteOpenHelper(context, "database.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, phone INT, imageId INT)",
        "INSERT INTO contact (name, email, phone, imageId) VALUES ('Gabriel', 'gabriel@gmail.com', 16999999999, -1)",
        "INSERT INTO contact (name, email, phone, imageId) VALUES ('Eliza', 'eliza@gmail.com', 16999999999, -1)"
    )

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertContact(name: String, email: String, phone: Int, imageId: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)
        val res = db.insert("contact", null, contentValues)
        db.close()
        return res
    }

    fun updateContact(id: Int, name: String, email: String, phone: Int, imageId: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)
        val res = db.update("contact", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun deleteContact(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("contact", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getContact(id: Int): ContactModel {
        val db = this.readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM contact WHERE id=?", arrayOf(id.toString()))

        var contactModel = ContactModel()

        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val phoneIndex = c.getColumnIndex("phone")
            val emailIndex = c.getColumnIndex("email")
            val imageIdIndex = c.getColumnIndex("imageId")

            contactModel = ContactModel(
                id = c.getInt(idIndex),
                name = c.getString(nameIndex),
                email = c.getString(emailIndex),
                phone = c.getInt(phoneIndex),
                imageId = c.getInt(imageIdIndex))
        }
        db.close()
        return contactModel
    }

    fun getAllContact(): ArrayList<ContactModel> {
        val db = this.readableDatabase
        val c = db.rawQuery(
        "SELECT * FROM contact", null)

        var listContactModel = ArrayList<ContactModel>()

        if (c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val phoneIndex = c.getColumnIndex("phone")
            val emailIndex = c.getColumnIndex("email")
            val imageIdIndex = c.getColumnIndex("imageId")
            do {
                val contactModel = ContactModel(
                    id = c.getInt(idIndex),
                    name = c.getString(nameIndex),
                    email = c.getString(emailIndex),
                    phone = c.getInt(phoneIndex),
                    imageId = c.getInt(imageIdIndex))
             listContactModel.add(contactModel)
            } while(c.moveToNext())
        }
        db.close()
        return listContactModel
    }
}