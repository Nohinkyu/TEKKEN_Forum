package com.nik.tkforum.ui.chatroom

import com.nik.tkforum.data.model.Chat

sealed class ChatType {

    data class ReceivedChat(val chat: Chat) : ChatType() {
        override val id: Chat
            get() = chat
    }

    data class SentChat(val chat: Chat) : ChatType() {
        override val id: Chat
            get() = chat
    }
    abstract val id: Chat
}
