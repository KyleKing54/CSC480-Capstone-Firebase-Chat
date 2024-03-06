package com.kingk.chat.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.Timestamp
import com.kingk.chat.objects.User
import java.text.SimpleDateFormat

class AndroidUtil {

    fun timestampToString (timestamp: Timestamp): String {
        return SimpleDateFormat("HH:MM").format(timestamp.toDate())
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