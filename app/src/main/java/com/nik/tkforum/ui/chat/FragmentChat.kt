package com.nik.tkforum.ui.chat

import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.ui.BaseFragment

class FragmentChat : BaseFragment() {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat
}