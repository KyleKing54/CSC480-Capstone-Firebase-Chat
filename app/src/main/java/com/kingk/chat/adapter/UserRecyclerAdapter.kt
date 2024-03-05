package com.kingk.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingk.chat.R
import com.kingk.chat.objects.User
import com.kingk.chat.screens.Conversation
import com.kingk.chat.utils.AndroidUtil
import com.kingk.chat.utils.FirebaseUtil


class UserRecyclerAdapter(private val userList : ArrayList<User>, private val context: Context) : RecyclerView.Adapter<UserRecyclerAdapter.UserModelViewHolder>() {

    val firebaseUtil = FirebaseUtil()
    val androidUtil = AndroidUtil()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserModelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_recycler_item, parent, false)
        return UserModelViewHolder(itemView)
    }

    // create user class, set values, pass to holder
    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int) {
        val user : User = userList[position]
        holder.username.text = user.username

        holder.itemView.setOnClickListener() {
            val intent = Intent(context, Conversation::class.java)
            intent.putExtra("username",user.username)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username : TextView = itemView.findViewById(R.id.username_text)
    }
}