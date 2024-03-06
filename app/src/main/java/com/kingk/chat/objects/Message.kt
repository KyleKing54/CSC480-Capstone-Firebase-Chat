package com.kingk.chat.objects

import com.google.firebase.Timestamp

data class Message(
    val text : String  ?= null,
    val senderID : String  ?= null,
    val timestamp : Timestamp  ?= null
)
