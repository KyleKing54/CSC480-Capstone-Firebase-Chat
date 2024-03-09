package com.kingk.chat.objects

import com.google.firebase.Timestamp
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MessageTest {

    private var testMessage = Message(
        "test message body text",
        "user1",
        Timestamp.now()
    )

    @Test
    fun getText() {
        assertEquals("test message body text", testMessage.text)
    }

    @Test
    fun getSenderID() {
        assertEquals("user1", testMessage.senderID)
    }

    @Test
    fun getTimestamp() {
        assertEquals(Timestamp.now(), testMessage.timestamp)
    }
}