package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kingk.chat.R
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth = Firebase.auth
    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // verify user is still logged in, if not send to login screen
        firebaseUtil.verifyLogin(this, auth)

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