package com.kingk.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kingk.chat.R
import com.kingk.chat.objects.Message
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil


class MessageRecyclerAdapter (
    private val messageList : ArrayList<Message>,
    private val context: Context
) : RecyclerView.Adapter<MessageRecyclerAdapter.ConversationViewHolder>() {

    private var androidUtil: AndroidUtil = AndroidUtil()
    private var firebaseUtil : FirebaseUtil = FirebaseUtil()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_recycler_item, parent, false)
        return ConversationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val message : Message = messageList[position]
        if (message.senderID.equals(firebaseUtil.getCurrentUserID())) {
            holder.ownLayout.isVisible = true
            holder.othersLayout.isVisible = false
            holder.ownMessage.text = message.text
        }
        else {
            holder.othersLayout.isVisible = true
            holder.ownLayout.isVisible = false
            holder.othersMessage.text = message.text
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ownLayout : LinearLayout = itemView.findViewById(R.id.own_layout)
        val othersLayout : LinearLayout = itemView.findViewById(R.id.others_layout)

        val ownMessage : TextView = itemView.findViewById(R.id.own_message)
        val othersMessage : TextView = itemView.findViewById(R.id.others_message)
    }
}