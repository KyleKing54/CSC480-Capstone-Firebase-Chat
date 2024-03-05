package com.kingk.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingk.chat.FirebaseUtil
import com.kingk.chat.R
import com.kingk.chat.User

class UserRecyclerAdapter(private val userList : ArrayList<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserModelViewHolder>() {

    val firebaseUtil = FirebaseUtil()

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
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username : TextView = itemView.findViewById(R.id.username_text)
    }
}