package com.kingk.chat.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {

    // returns the user's UID from their authentication account
    fun currentUserId(): String {
        return Firebase.auth.currentUser!!.uid
    }

    // returns the user's stored data from the Firestore database "users"
    fun currentUserData(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserId())
    }
}