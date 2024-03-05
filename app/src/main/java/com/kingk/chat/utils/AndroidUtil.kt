package com.kingk.chat.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kingk.chat.objects.User
import com.kingk.chat.screens.Login
import com.kingk.chat.screens.MainActivity

class AndroidUtil {

    // Verify user is logged in on screen switch
    fun checkIfLoggedIn(context: Context, auth : FirebaseAuth) {
        if (auth.currentUser != null) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        else {
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }

    fun showToast(context: Context, message : String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun passUserIntent(intent: Intent, user: com.kingk.chat.objects.User) {
        intent.putExtra("userID", user.userID)
        intent.putExtra("username", user.username)
        intent.putExtra("email", user.email)
    }

    fun receiveUserIntent(intent: Intent): User {
        return User(
            intent.getStringExtra("userID"),
            intent.getStringExtra("username"),
            intent.getStringExtra("email")
        )
    }
}