package com.kingk.chat.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kingk.chat.R
import com.kingk.chat.adapter.RecentConvoRecyclerAdapter
import com.kingk.chat.objects.Conversation
import com.kingk.chat.utils.FirebaseUtil

class MainActivity : AppCompatActivity() {

    private val auth : FirebaseAuth = Firebase.auth
    private val firebaseUtil : FirebaseUtil = FirebaseUtil()

    private lateinit var db : FirebaseFirestore
    private lateinit var convoArrayList : ArrayList<Conversation>
    private lateinit var adapter : RecentConvoRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // verify user is still logged in, if not send to login screen
        firebaseUtil.verifyLogin(this, auth)

        // initialize UI objects
        val newConvoButton = findViewById<ExtendedFloatingActionButton>(R.id.new_conversation_button)
        val logoutButton = findViewById<ImageButton>(R.id.logout_button)
        val recentConversationRecycler = findViewById<RecyclerView>(R.id.recent_conversations_recycler)

        // configure the recyclerview and its adapter
        convoArrayList = arrayListOf()
        adapter = RecentConvoRecyclerAdapter(convoArrayList, this)
        recentConversationRecycler.adapter = adapter
        recentConversationRecycler.layoutManager = LinearLayoutManager(this)

        // start change manager to load data from Firestore into recyclerview
        convoChangeManager()

        // configure new convo button
        newConvoButton.setOnClickListener {
           startActivity(Intent(this, NewConversation::class.java))
        }

        // configure logout button
        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, Login::class.java))
        }
    }

    // loads user's conversation data pulled from the Firestore db into the recyclerview
    private fun convoChangeManager() {
        db = FirebaseFirestore.getInstance()
        db
            .collection("Conversations")
            .whereArrayContains("users", firebaseUtil.getCurrentUserID())
            .orderBy("lastTimeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore DB error", error.message.toString())
                    return@addSnapshotListener
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        convoArrayList.add(dc.document.toObject(Conversation::class.java))
                    }
                }

                //adapter.notifyDataSetChanged()
                adapter.notifyItemChanged(adapter.itemCount - 1)
            }
    }
}