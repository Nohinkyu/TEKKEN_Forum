package com.nik.tkforum.ui.chat

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.ui.BaseFragment

class ChatFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbChat.setOnMenuItemClickListener { menuItme ->
            when (menuItme.itemId) {
                R.id.action_add_chat_room -> {
                    findNavController().navigate(ChatFragmentDirections.actionNavChatToNavChatRoom())
                    true
                }
                else -> false
            }
        }
    }
}