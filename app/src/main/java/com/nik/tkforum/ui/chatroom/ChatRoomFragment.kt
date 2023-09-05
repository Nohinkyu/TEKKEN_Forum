package com.nik.tkforum.ui.chatroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.data.Chat
import com.nik.tkforum.data.User
import com.nik.tkforum.databinding.FragmentChatRoomBinding
import com.nik.tkforum.repository.ChatRoomRepository
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants

class ChatRoomFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatRoomBinding
    override val layoutId: Int get() = R.layout.fragment_chat_room

    private val args: ChatRoomFragmentArgs by navArgs()

    private val viewModel by viewModels<ChatRoomViewModel> {
        ChatRoomViewModel.provideFactory(
            repository = ChatRoomRepository(
                TekkenForumApplication.appContainer.provideGoogleApiClient()
            )
        )
    }

    private val user = User(
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_NICKNAME, ""),
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        deleteChatRoom()
        binding.chatRoomTitle = args.chatRoomHostName
        viewModel.addChatListener(args.chatRoomKey)

        val editText = binding.etChat
        binding.btSend.setOnClickListener {
            if (!editText.text.isNullOrEmpty()) {
                sendChat()
                editText.text = null
            }
        }
    }

    private fun setLayout() {
        val adapter = ChatListAdapter()
        binding.rvChatList.adapter = adapter
        viewModel.loadChatList(args.chatRoomKey)
        viewModel.chatList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rvChatList.scrollToPosition(adapter.currentList.size - 1)
        }
    }

    private fun sendChat() {
        val chat = Chat(
            user.profileUri,
            user.nickname,
            binding.etChat.text.toString(),
            user.email
        )
        viewModel.sendChat(args.chatRoomKey, chat)
    }

    private fun deleteChatRoom() {
        val action = ChatRoomFragmentDirections.actionNavChatRoomToNavChat()
        binding.ibDeleteChatRoom.setOnClickListener {
            if (args.chatRoomHostName == user.nickname) {
                viewModel.deleteChatRoom(args.chatRoomKey)
                findNavController().navigate(action)
            } else {
                Snackbar.make(binding.root, R.string.not_host, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}