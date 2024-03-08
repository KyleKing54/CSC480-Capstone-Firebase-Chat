package com.kingk.chat.objects

import com.google.firebase.Timestamp

data class Conversation (
    val conversationID : String ?= null,
    val users : List<String> ?= null,
    var lastMessage : String ?= null,
    var lastTimeStamp : Timestamp ?= null,
)