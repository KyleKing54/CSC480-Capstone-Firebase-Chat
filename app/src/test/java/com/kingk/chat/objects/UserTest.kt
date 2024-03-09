package com.kingk.chat.objects

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UserTest {

    private var testUser = User (
        "user1",
        "username1",
        "test@email.com"
    )

    @Test
    fun getUserID() {
        assertEquals("user1", testUser.userID)
    }

    @Test
    fun getUsername() {
        assertEquals("username1", testUser.username)
    }

    @Test
    fun getEmail() {
        assertEquals("test@email.com", testUser.email)
    }
}