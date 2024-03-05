package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kingk.chat.R
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class Login : AppCompatActivity() {

    private var auth : FirebaseAuth = Firebase.auth
    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // if user is already logged in, skip login
        firebaseUtil.skipLogin(this, auth)

        // initialize UI objects
        val editTextEmail = findViewById<TextInputEditText>(R.id.email)
        val editTextPassword = findViewById<TextInputEditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val switchRegister = findViewById<TextView>(R.id.switch_to_register)

        loginButton.setOnClickListener {

            // get input text after button is pushed
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // validate email input
            if (email == "") {
                androidUtil.showToast(this, "Please enter an email")
                return@setOnClickListener
            }

            // validate password input
            if (password == "") {
                androidUtil.showToast(this, "Please enter a password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // sign in success
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // sign in failed, alert the user
                        androidUtil.showToast(this, "Authentication failed")
                    }
                }
        }

        switchRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}