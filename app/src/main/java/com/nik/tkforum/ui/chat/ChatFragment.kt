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
import com.nik.tkforum.data.ChatRoomInfo
import com.nik.tkforum.data.User
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.ChatRoomClickListener
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat

    private val appContainer = TekkenForumApplication.appContainer
    private val apiClient = appContainer.provideGoogleApiClient()

    private val user = User(
        preferencesManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        preferencesManager.getString(Constants.KEY_NICKNAME, ""),
        preferencesManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    private val chatRoomClickListener = object : ChatRoomClickListener {
        override fun chatRoomClick(chatRoomKey: String) {
            val action = ChatFragmentDirections.actionNavChatToNavChatRoom(chatRoomKey)
            findNavController().navigate(action)
            lifecycleScope.launch {
                val response = apiClient.getChatRoomList()
                try {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            if (!data.getValue(chatRoomKey).userList.values.contains(user)) {
                                apiClient.joinUser(chatRoomKey, user)
                            }
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
        lifecycleScope.launch {
            apiClient.createChatRoom(chatRoom)
            val response = apiClient.getChatRoomList()
            try {
                var lastChatRoomKey = ""
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        lastChatRoomKey = data.keys.last()
                        findNavController().navigate(
                            ChatFragmentDirections.actionNavChatToNavChatRoom(
                                lastChatRoomKey
                            )
                        )

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


    private fun setLayout() {
        val chatRoomList = mutableListOf<ChatRoomInfo>()
        val adapter = ChatRoomListAdapter(chatRoomClickListener)
        binding.rvChatRoomList.adapter = adapter
        lifecycleScope.launch {
            val response = apiClient.getChatRoomList()
            try {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        for (chatRoom in data) {
                            chatRoomList.add(ChatRoomInfo(chatRoom.key, chatRoom.value))
                        }
                        adapter.submitList(chatRoomList)
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