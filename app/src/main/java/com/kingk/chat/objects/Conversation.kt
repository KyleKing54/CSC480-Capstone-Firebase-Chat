package com.kingk.chat.objects

import com.google.firebase.Timestamp

class Conversation (
    conversationID : String ?= null,
    users : List<String> ?= null,
    lastTimeStamp : Timestamp ?= null,
    lastSender : String ?= null
)