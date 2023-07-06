package com.nik.tkforum.ui.chatroom

import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentChatRoomBinding
import com.nik.tkforum.ui.BaseFragment

class ChatRoomFragment: BaseFragment() {

    override val binding get() = _binding as FragmentChatRoomBinding
    override val layoutId: Int get() = R.layout.fragment_chat_room
}