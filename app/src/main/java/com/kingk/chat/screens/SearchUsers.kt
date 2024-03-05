package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kingk.chat.R
import com.kingk.chat.adapter.UserRecyclerAdapter
import com.kingk.chat.objects.User

class SearchUsers : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var userArrayList : ArrayList<User>
    private lateinit var adapter : UserRecyclerAdapter
    private lateinit var userRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)

        // initialize UI objects
        val searchButton = findViewById<ImageButton>(R.id.search_users_button)
        val searchInputText = findViewById<EditText>(R.id.username_edit_text)
        val userRecyclerView = findViewById<RecyclerView>(R.id.search_recycler)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        //searchInputText.requestFocus()

        userArrayList = arrayListOf()
        adapter = UserRecyclerAdapter(userArrayList, this)

        userRecyclerView.adapter = adapter
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        eventChangeManager()

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        searchButton.setOnClickListener() {

            val searchText = searchInputText.text.toString()

            if (searchText.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Invalid Search",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            searchUserRecycler(searchText)
        }
    }

    private fun searchUserRecycler(searchText: String) {
        //adapter = UserRecyclerAdapter(,applicationContext)

    }

    private fun eventChangeManager() {
        db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .orderBy("username", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore DB error", error.message.toString())
                return@addSnapshotListener
            }
            for (dc : DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    userArrayList.add(dc.document.toObject(User::class.java))
                }
            }

            adapter.notifyDataSetChanged()
        }
    }
}