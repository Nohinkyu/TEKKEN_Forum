package com.nik.tkforum.data

data class ChatRoom(
    val createUserName: String,
    val createUserProfile: String,
    val userList: List<User>,
    val chatList: Map<String, Chat> = emptyMap() ,
)
