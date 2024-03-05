package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kingk.chat.R
import com.kingk.chat.objects.Conversation
import com.kingk.chat.objects.User
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class SelectedConversation : AppCompatActivity() {

    private var auth : FirebaseAuth = Firebase.auth
    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    private lateinit var conversation : Conversation
    lateinit var receivedUser : User
    lateinit var conversationID : String
    //private var receivedUser : User = androidUtil.receiveUserIntent(intent)
    //private var conversationID = firebaseUtil.generateConversationID(firebaseUtil.currentUserId(), receivedUser.userID.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        // verify user is still logged in, if not send to login screen
        firebaseUtil.verifyLogin(this, auth)

        receivedUser = androidUtil.receiveUserIntent(intent)

        // initialize UI objects
        val messageBox = findViewById<EditText>(R.id.message_box)
        val sendButton = findViewById<ImageButton>(R.id.send_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val otherPerson = findViewById<TextView>(R.id.received_username)
        val messageRecyclerView = findViewById<RecyclerView>(R.id.message_recycler_view)

        conversationID = firebaseUtil.generateConversationID(firebaseUtil.currentUserId(), receivedUser.userID.toString())

        //val conversationID = firebaseUtil.generateConversationID(firebaseUtil.currentUserId(), receivedUser.userID.toString())
        //val conversationID = firebaseUtil.generateConversationID(firebaseUtil.currentUserId(), receivedUser.userID.toString())
        otherPerson.text = receivedUser.username

        loadConversation()

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun loadConversation() {
        firebaseUtil.findConversation(conversationID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tempConvo: Conversation? = task.result.toObject(Conversation::class.java)
                if (tempConvo != null) {
                    conversation = tempConvo
                }
                else {
                    // no conversation found, create a new one
                    conversation = Conversation(
                        conversationID,
                        listOf<String>(firebaseUtil.currentUserId(), receivedUser.userID.toString()),
                        Timestamp.now(),
                        ""
                    )
                    firebaseUtil.findConversation(conversationID).set(conversation)
                }
            }
        }
    }
}