package com.nik.tkforum.data

data class ChatRoom(
    val createUserName: String,
    val createUserProfile: String,
    val userList: Map<String, User> = emptyMap(),
    val chatList: Map<String, Chat> = emptyMap() ,
)
