package com.nik.tkforum.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.TekkenForumApplication.Companion.preferencesManager
import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.data.User
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.repository.ChatRoomListRepository
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.ChatRoomClickListener
import com.nik.tkforum.util.Constants

class ChatFragment : BaseFragment(), ChatRoomClickListener {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat

    private val viewModel by viewModels<ChatRoomListViewModel> {
        ChatRoomListViewModel.provideFactory(
            repository = ChatRoomListRepository(
                TekkenForumApplication.appContainer.provideGoogleApiClient()
            )
        )
    }

    private val user = User(
        preferencesManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        preferencesManager.getString(Constants.KEY_NICKNAME, ""),
        preferencesManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        binding.tbChat.setOnMenuItemClickListener { menuItme ->
            when (menuItme.itemId) {
                R.id.action_add_chat_room -> {
                    createChatRoom()
                    true
                }

                else -> false
            }
        }
    }

    private fun createChatRoom() {
        val chatRoom =
            ChatRoom(user.nickname, user.profileUri, mapOf(Constants.CREATOR_KET to user))
        viewModel.createChatRoom(chatRoom)
        viewModel.getCreateChatRoomKey()
        viewModel.lastChatRoomKey.observe(viewLifecycleOwner) { lastChatRoomKey ->
            findNavController().navigate(
                ChatFragmentDirections.actionNavChatToNavChatRoom(
                    lastChatRoomKey, user.nickname
                )
            )
        }
    }

    private fun setLayout() {
        val adapter = ChatRoomListAdapter(this)
        binding.rvChatRoomList.adapter = adapter
        viewModel.loadAllChatRoom()
        viewModel.chatRoomList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun chatRoomClick(chatRoomKey: String, hostName: String) {
        val action = ChatFragmentDirections.actionNavChatToNavChatRoom(chatRoomKey, hostName)
        findNavController().navigate(action)
        viewModel.joinUser(chatRoomKey, user)
    }
}
