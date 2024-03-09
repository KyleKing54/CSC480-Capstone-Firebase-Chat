package com.kingk.chat.objects

import com.google.firebase.Timestamp
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


class ConversationTest {

    private var testConversation = Conversation (
        "testID",
        listOf("user1", "user2"),
        "test last message",
        Timestamp.now()
    )

    @Test
    fun getConversationID() {
        assertEquals("testID", testConversation.conversationID)
    }

    @Test
    fun getUsers() {
        assertEquals(listOf("user1", "user2"), testConversation.users)
    }

    @Test
    fun getLastMessage() {
        assertEquals("test last message", testConversation.lastMessage)
    }

    @Test
    fun setLastMessage() {
        testConversation.lastMessage = "new last message"
        assertEquals("new last message", testConversation.lastMessage)
    }

    @Test
    fun getLastTimeStamp() {
        assertEquals(Timestamp.now(), testConversation.lastTimeStamp)
    }
}