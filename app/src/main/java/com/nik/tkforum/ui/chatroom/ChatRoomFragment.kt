package com.nik.tkforum.ui.chatroom

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.data.Chat
import com.nik.tkforum.databinding.FragmentChatRoomBinding
import com.nik.tkforum.ui.BaseFragment
import kotlinx.coroutines.launch

class ChatRoomFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatRoomBinding
    override val layoutId: Int get() = R.layout.fragment_chat_room

    private val result = mutableListOf<ChatType>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        val editText = binding.etChat
        editText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editText.text.isNullOrEmpty()) {
                    return@setOnKeyListener true
                }
                sendChat()
                editText.text = null
            }
            false
        }
    }

    private fun setLayout() {
        val adapter = ChatListAdapter()
        binding.rvChatList.adapter = adapter
        val appContainer = TekkenForumApplication.appContainer
        val apiClient = appContainer.provideGoogleApiClient()
        lifecycleScope.launch {
            val response = apiClient.getChatList()
            try {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        setAdapterChatList(data.values.toList())
                        adapter.submitList(result)
                    }
                } else {
                    val errorMessage = response.errorBody().toString()
                    Log.d("ChatFragment", errorMessage)
                }
            } catch (e: Exception) {
                Log.d("ChatFragment", "데이터를 가져오지 못했습니다.")
            }
        }
    }

    private fun setAdapterChatList(chatList: List<Chat>) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        for (chat in chatList) {
            if (chat.senderEmail == email) {
                result.add(ChatType.SentChat(chat))
            } else
                result.add(ChatType.ReceivedChat(chat))
        }
    }

    private fun sendChat() {
        val appContainer = TekkenForumApplication.appContainer
        val apiClient = appContainer.provideGoogleApiClient()
        val firebase = FirebaseAuth.getInstance().currentUser
        val chat = Chat(
            firebase?.photoUrl.toString(),
            firebase?.displayName.toString(),
            binding.etChat.text.toString(),
            firebase?.email.toString()
        )
        lifecycleScope.launch {
            apiClient.sendChat(chat)
        }
    }
}