package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.kingk.chat.R

class Conversation : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        auth = Firebase.auth
        val user : FirebaseUser? = auth.currentUser

        // if user is null, open login activity
        if (user == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        // initialize UI objects
        val messageBox = findViewById<EditText>(R.id.message_box)
        val sendButton = findViewById<ImageButton>(R.id.send_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val otherPerson = findViewById<TextView>(R.id.received_username)
        val messageRecyclerView = findViewById<RecyclerView>(R.id.message_recycler_view)

        //val receivedUserName = intent.getStringExtra("username")
        otherPerson.text = intent.getStringExtra("username")

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}