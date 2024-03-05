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

class Login : AppCompatActivity() {

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
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

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

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // sign in success
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // sign in failed, alert the user
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        switchRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            finish()
        }
    }
}