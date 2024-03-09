package com.kingk.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingk.chat.R
import com.kingk.chat.objects.User
import com.kingk.chat.screens.ActiveConversation
import com.kingk.chat.utils.AndroidUtil
import java.util.Locale


class UserRecyclerAdapter(
    private val userList : ArrayList<User>,
    private val context: Context
) : RecyclerView.Adapter<UserRecyclerAdapter.UserModelViewHolder>(), Filterable {

    private var androidUtil: AndroidUtil = AndroidUtil()
    private var userFilterList: ArrayList<User>

    // set the filter to all users in the list on construction
    init {
        userFilterList = userList
    }

    // inflate item in recyclerview and return view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserModelViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.user_recycler_item, parent, false)
        return UserModelViewHolder(itemView)
    }

    // load items into holder
    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int) {
        val user : User = userFilterList[position]
        holder.username.text = user.username

        // set onclick listener to open the clicked on conversation
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ActiveConversation::class.java)
            androidUtil.passUserIntent(intent, user)
            context.startActivity(intent)
        }
    }

    // filter out user's based on user's input into filter text box
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchInput = constraint.toString()
                // if input is empty, don't filter any users out
                userFilterList = if (searchInput.isEmpty()) {
                    userList
                } else {
                    // create variable to hold results of the filter
                    val resultList = ArrayList<User>()
                    for (item in userList) {
                        // loop through user list, if user's username contains the searchInput, add it to the results list
                        if (item.username!!.lowercase(Locale.ENGLISH).contains(searchInput.lowercase(Locale.ENGLISH))) {
                            resultList.add(item)
                        }
                    }
                    // return all users not filtered out
                    resultList
                }
                // create a filter result, load in our results, then return the results
                val filterResult = FilterResults()
                filterResult.values = userFilterList
                return filterResult
            }

            // add the filtered results to the list to be created
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // suppressing the warning, the data will always be the correct type
                @Suppress("UNCHECKED_CAST")
                userFilterList = results?.values as ArrayList<User>
                //notifyDataSetChanged()
                notifyItemChanged(userFilterList.size)
            }

        }
    }

    override fun getItemCount(): Int {
        return userFilterList.size
    }

    class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username : TextView = itemView.findViewById(R.id.username_text)
    }
}