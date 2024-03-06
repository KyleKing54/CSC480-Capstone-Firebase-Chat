package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kingk.chat.R
import com.kingk.chat.adapter.MessageRecyclerAdapter
import com.kingk.chat.objects.Conversation
import com.kingk.chat.objects.Message
import com.kingk.chat.objects.User
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil

class SelectedConversation : AppCompatActivity() {

    private var auth : FirebaseAuth = Firebase.auth
    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    private lateinit var conversation : Conversation
    private lateinit var receivedUser : User
    private lateinit var conversationID : String
    private lateinit var messageBox : EditText

    private lateinit var db : FirebaseFirestore
    private lateinit var messageArrayList : ArrayList<Message>
    private lateinit var adapter : MessageRecyclerAdapter
    lateinit var messageRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        // verify user is still logged in, if not send to login screen
        firebaseUtil.verifyLogin(this, auth)

        receivedUser = androidUtil.receiveUserIntent(intent)

        // initialize UI objects
        messageBox = findViewById<EditText>(R.id.message_box)
        val sendButton = findViewById<ImageButton>(R.id.send_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val otherPerson = findViewById<TextView>(R.id.received_username)
        val messageRecyclerView = findViewById<RecyclerView>(R.id.message_recycler_view)

        conversationID = firebaseUtil.generateConversationID(firebaseUtil.getCurrentUserID(), receivedUser.userID.toString())

        otherPerson.text = receivedUser.username

        loadConversation()

        messageArrayList = arrayListOf()
        adapter = MessageRecyclerAdapter(messageArrayList, this)

        messageRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true

        messageRecyclerView.layoutManager = LinearLayoutManager(this)



        messageChangeManager()


        sendButton.setOnClickListener() {
            val typedMessage = messageBox.text.toString().trim()
            if (typedMessage.isEmpty()) {
                return@setOnClickListener
            }
            sendMessageToConversation(typedMessage)
        }

        backButton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun loadConversation() {
        firebaseUtil.getConversationID(conversationID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tempConvo: Conversation? = task.result.toObject(Conversation::class.java)
                if (tempConvo != null) {
                    conversation = tempConvo
                }
                else {
                    // no conversation found, create a new one
                    conversation = Conversation(
                        conversationID,
                        listOf<String>(firebaseUtil.getCurrentUserID(), receivedUser.userID.toString()),
                        "",
                        Timestamp.now(),
                        ""
                    )
                    firebaseUtil.getConversationID(conversationID).set(conversation)
                }
            }
        }
    }

    private fun sendMessageToConversation(messageText : String) {
        // update the conversation data
        conversation.lastTimeStamp = Timestamp.now()
        conversation.lastSender = firebaseUtil.getCurrentUserID()
        conversation.lastMessage = messageText
        firebaseUtil.getConversationID(conversationID).set(conversation)

        val message = Message(messageText, firebaseUtil.getCurrentUserID(), Timestamp.now())
        firebaseUtil.getConversationMessages(conversationID).add(message).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                messageBox.setText("")
            }
        }
    }

    private fun messageChangeManager() {
        db = FirebaseFirestore.getInstance()
        db
            .collection("Conversations/$conversationID/Messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore DB error", error.message.toString())
                    return@addSnapshotListener
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        messageArrayList.add(dc.document.toObject(Message::class.java))
                    }
                }

                // this doesn't work
                adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        messageRecyclerView.smoothScrollToPosition(0)
                    }
                })

                adapter.notifyDataSetChanged()
            }
    }

}