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

    private val auth : FirebaseAuth = Firebase.auth
    private val androidUtil: AndroidUtil = AndroidUtil()
    private val firebaseUtil : FirebaseUtil = FirebaseUtil()


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
            if (!androidUtil.testEmailInput(this, email)) {
                return@setOnClickListener
            }

            // validate password is not empty to prevent crashes
            if (password == "") {
                androidUtil.showToast(this, "Please enter a password")
                return@setOnClickListener
            }

            // checks passed, attempt login via Firebase
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

        // switch to register a new user instead
        switchRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}