package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kingk.chat.R
import com.kingk.chat.adapter.UserRecyclerAdapter
import com.kingk.chat.objects.User
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class NewConversation : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var userArrayList : ArrayList<User>
    private lateinit var adapter : UserRecyclerAdapter

    private val auth : FirebaseAuth = Firebase.auth
    private val androidUtil: AndroidUtil = AndroidUtil()
    private val firebaseUtil : FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)

        // verify user is still logged in, if not send to login screen
        firebaseUtil.verifyLogin(this, auth)

        // initialize UI objects
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<ImageButton>(R.id.search_users_button)
        val userRecyclerView = findViewById<RecyclerView>(R.id.user_recycler)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        // configure the recyclerview and its adapter
        userArrayList = arrayListOf()
        adapter = UserRecyclerAdapter(userArrayList, this)
        userRecyclerView.adapter = adapter
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        // start change manager to load data from Firestore into recyclerview
        eventChangeManager()

        // configure back button
        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // loads register user data pulled from the Firestore db into the recyclerview
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