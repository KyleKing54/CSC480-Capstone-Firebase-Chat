package com.kingk.chat.utils

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.widget.Toast
import com.google.firebase.Timestamp
import com.kingk.chat.objects.User
import java.text.SimpleDateFormat
import java.util.Locale

class AndroidUtil {

    // validate user's input is acceptable for email field
    fun testEmailInput (context: Context, input: String): Boolean {
        // check if field was empty
        if (input.isEmpty()) {
            showToast(context, "Please enter an email")
            return false
        }

        // check if input was an email address
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            showToast(context, "Please enter a valid email address")
            return false
        }

        // all checks passed
        return true
    }

    // validate user's input is acceptable for username field
    fun testUsernameInput (context: Context, input: String): Boolean {
        // set min acceptable username length
        val minUsernameLength = 3

        // check if field was empty
        if (input.isEmpty()) {
            showToast(context, "Please enter a username")
            return false
        }

        // check if input was longer than min required length
        if (input.length <= minUsernameLength) {
            showToast(context, "Minimum required username length is $minUsernameLength")
            return false
        }

        // all checks passed
        return true
    }

    // validate user's input is acceptable for password field
    fun testPasswordInput (context: Context, input: String): Boolean {

        val minPasswordLength = 8

        // check if field was empty
        if (input.isEmpty()) {
            showToast(context, "Please enter a password")
            return false
        }

        // check if input was longer than min required length
        if (input.length <= minPasswordLength) {
            showToast(context, "Minimum required username length is $minPasswordLength")
            return false
        }

        // all checks passed
        return true
    }

    // converts timestamp from Firebase into human readable number
    fun convertTimestamp (timestamp: Timestamp): String {
        // if the date is from today, display the time the message was sent, otherwise the day and year
        return if (DateUtils.isToday(timestamp.toDate().time)) {
            SimpleDateFormat("h:mm a", Locale.ENGLISH).format(timestamp.toDate())
        } else {
            SimpleDateFormat("MMM d yyyy", Locale.ENGLISH).format(timestamp.toDate())
        }
    }

    // show a toast in the context of the activity requesting it
    fun showToast(context: Context, message : String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    // passes a user object by intent so one activity can send user data to another
    fun passUserIntent(intent: Intent, user: User) {
        intent.putExtra("userID", user.userID)
        intent.putExtra("username", user.username)
        intent.putExtra("email", user.email)
    }

    // catch the passed user object in the activity that needs it
    fun receiveUserIntent(intent: Intent): User {
        return User(
            intent.getStringExtra("userID"),
            intent.getStringExtra("username"),
            intent.getStringExtra("email")
        )
    }
}