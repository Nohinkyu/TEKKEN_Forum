package com.nik.tkforum.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.TekkenForumApplication.Companion.preferencesManager
import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.data.User
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat

    private val appContainer = TekkenForumApplication.appContainer
    private val apiClient = appContainer.provideGoogleApiClient()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        binding.tbChat.setOnMenuItemClickListener { menuItme ->
            when (menuItme.itemId) {
                R.id.action_add_chat_room -> {
                    findNavController().navigate(ChatFragmentDirections.actionNavChatToNavChatRoom())
                    createChatRoom()
                    true
                }

                else -> false
            }
        }
    }

    private fun createChatRoom() {
        val creator = User(
            preferencesManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
            preferencesManager.getString(Constants.KEY_NICKNAME, ""),
            preferencesManager.getString(Constants.KEY_MAIL_ADDRESS, "")
        )
        val chatRoom =
            ChatRoom(creator.nickname, creator.profileUri, listOf<User>(creator))
        lifecycleScope.launch {
            apiClient.createChatRoom(chatRoom)
        }
    }

    private fun setLayout() {
        val adapter = ChatRoomListAdapter()
        binding.rvChatRoomList.adapter = adapter
        lifecycleScope.launch {
            val response = apiClient.getChatRoomList()
            try {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        adapter.submitList(data.values.toList())
                    }
                } else {
                    val errorMessage = response.errorBody().toString()
                    Log.d("ChatRoomListFragment", errorMessage)
                }
            } catch (e: Exception) {
                Log.d("ChatRoomListFragment", "데이터를 가져오지 못했습니다.")
            }
        }
    }
}