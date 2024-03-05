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
import com.kingk.chat.objects.User
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class Register : AppCompatActivity() {

    private var auth : FirebaseAuth = Firebase.auth
    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // if user is already logged in, skip registration
        firebaseUtil.skipLogin(this, auth)

        // initialize UI objects
        val editTextEmail = findViewById<TextInputEditText>(R.id.email)
        val editUsername = findViewById<TextInputEditText>(R.id.username)
        val editTextPassword = findViewById<TextInputEditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.register_button)
        val switchLogin = findViewById<TextView>(R.id.switch_to_login)

        registerButton.setOnClickListener {

            // get input text after button is pushed
            val email = editTextEmail.text.toString()
            val username = editUsername.text.toString()
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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, display a message to the user.
                        androidUtil.showToast(this, "Account created")

                        // create user object
                        val user = User(
                            firebaseUtil.currentUserId(),
                            username,
                            email
                        )

                        // add user to database
                        firebaseUtil.currentUserData().set(user).addOnCompleteListener() {
                            if (task.isSuccessful) {
                                // Log the user in
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        androidUtil.showToast(this, "Authentication failed")
                    }
                }
            }

        switchLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
}