package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.kingk.chat.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val user : FirebaseUser? = auth.currentUser

        // if user is null, open login activity
        if (user == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        // initialize UI objects
        val searchButton = findViewById<ImageButton>(R.id.search_button)
        val logoutButton = findViewById<ImageButton>(R.id.logout_button)

        searchButton.setOnClickListener() {
           startActivity(Intent(this, SearchUsers::class.java))
           finish()
        }

        logoutButton.setOnClickListener() {
            Firebase.auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}