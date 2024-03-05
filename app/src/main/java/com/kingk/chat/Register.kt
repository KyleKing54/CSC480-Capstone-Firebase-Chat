package com.kingk.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Register : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        val firebaseUtil = FirebaseUtil()

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
                Toast.makeText(
                    baseContext,
                    "Please enter an email",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // validate password input
            if (password == "") {
                Toast.makeText(
                    baseContext,
                    "Please enter an email",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        // create user class
                        val user = User(
                            firebaseUtil.currentUserId(),
                            username,
                            email
                        )

                        // add user to database
                        firebaseUtil.currentUserData().set(user).addOnCompleteListener() {
                            if (task.isSuccessful) {
                                // Log the user in
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                        }


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

        switchLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}