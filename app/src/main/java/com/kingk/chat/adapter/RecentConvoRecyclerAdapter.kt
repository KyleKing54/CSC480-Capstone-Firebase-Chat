package com.kingk.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingk.chat.R
import com.kingk.chat.objects.Conversation
import com.kingk.chat.objects.User
import com.kingk.chat.screens.ActiveConversation
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil


class RecentConvoRecyclerAdapter (
    private val convoList : ArrayList<Conversation>,
    private val context: Context
) : RecyclerView.Adapter<RecentConvoRecyclerAdapter.ConvoViewHolder>() {

    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConvoViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recent_convo_item, parent, false)
        return ConvoViewHolder(itemView)
    }

    // create user class, set values, pass to holder
    override fun onBindViewHolder(holder: ConvoViewHolder, position: Int) {
        val conversation : Conversation = convoList[position]

        conversation.users?.let {
            firebaseUtil.getConversationPartner(it).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val convoPartner : User = task.result.toObject(User::class.java)!!
                    holder.username.text = convoPartner.username
                    holder.lastMessage.text = conversation.lastMessage
                    holder.timestamp.text = conversation.lastTimeStamp?.let { it1 ->
                        androidUtil.convertTimestamp(
                            it1
                        )
                    }

                    holder.itemView.setOnClickListener() {
                        val intent = Intent(context, ActiveConversation::class.java)
                        androidUtil.passUserIntent(intent, convoPartner)
                        context.startActivity(intent)
                    }
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return convoList.size
    }

    class ConvoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username : TextView = itemView.findViewById(R.id.username_text)
        val lastMessage : TextView = itemView.findViewById(R.id.last_message)
        val timestamp : TextView = itemView.findViewById(R.id.timestamp)
    }
}