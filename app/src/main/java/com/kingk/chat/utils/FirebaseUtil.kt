package com.kingk.chat.utils

import android.content.Context
import android.content.Intent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kingk.chat.screens.Login
import com.kingk.chat.screens.MainActivity

class FirebaseUtil {

    // check if user is already logged in, skip login if they are
    fun skipLogin(context: Context, auth : FirebaseAuth) {
        if (auth.currentUser != null) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    // verify user is still logged in between screen switching, send to login if they are not
    fun verifyLogin(context: Context, auth : FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)
        }
    }

    // returns the user's UID from their authentication account
    fun getCurrentUserID(): String {
        return Firebase.auth.currentUser!!.uid
    }

    // returns all users in the "Users" db
    private fun getAllUsers() : CollectionReference {
        return FirebaseFirestore.getInstance().collection("Users")
    }

    // returns the user's stored data from the Firestore database "users"
    fun currentUserData(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("Users").document(getCurrentUserID())
    }

    // combine two userID's into a unique hash value for their conversationID
    fun generateConversationID (userID1: String, userID2 : String): String {
        // TODO build an actual hash here
        return if (userID1.hashCode() < userID2.hashCode()) {
            userID1 + "_" + userID2
        } else {
            userID2 + "_" + userID1
        }
    }

    // returns the unique conversation ID hash from Firestore, if one exists
    fun getConversationID (conversationID : String): DocumentReference {
        return FirebaseFirestore.getInstance().collection("Conversations").document(conversationID)
    }

    // returns all messages in a specific conversation ID
    fun getConversationMessages (conversationID : String): CollectionReference {
        return getConversationID(conversationID).collection("Messages")
    }

    // returns the other user's username in a conversation based on their userID
    fun getConversationPartner (userIDs : List<String>): DocumentReference {
        return if (userIDs[0] == getCurrentUserID()) {
            getAllUsers().document(userIDs[1])
        } else {
            getAllUsers().document(userIDs[0])
        }
    }
}