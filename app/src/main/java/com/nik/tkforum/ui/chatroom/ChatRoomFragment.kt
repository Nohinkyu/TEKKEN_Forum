package com.nik.tkforum.ui.chatroom

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.data.Chat
import com.nik.tkforum.data.User
import com.nik.tkforum.databinding.FragmentChatRoomBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.launch

class ChatRoomFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatRoomBinding
    override val layoutId: Int get() = R.layout.fragment_chat_room

    private val appContainer = TekkenForumApplication.appContainer
    private val apiClient = appContainer.provideGoogleApiClient()
    private val args: ChatRoomFragmentArgs by navArgs()

    private val adapter = ChatListAdapter()

    private var result = mutableListOf<ChatType>()

    private val user = User(
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_NICKNAME, ""),
        TekkenForumApplication.preferencesManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        updateChat()

        val editText = binding.etChat
        binding.btSend.setOnClickListener {
            if (!editText.text.isNullOrEmpty()) {
                sendChat()
                editText.text = null
            }
        }
    }

    private fun setLayout() {
        binding.rvChatList.adapter = adapter
        lifecycleScope.launch {
            val response = apiClient.getChatList(args.chatRoomKey)
            try {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        setAdapterChatList(data.values.toList())
                        adapter.submitList(result)
                        if (result.size > 0) {
                            binding.rvChatList.scrollToPosition(result.size - 1)
                        }
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
        val chat = Chat(
            user.profileUri,
            user.nickname,
            binding.etChat.text.toString(),
            user.email
        )
        lifecycleScope.launch {
            apiClient.sendChat(args.chatRoomKey, chat)
        }
    }

    private fun updateChat() {
        binding.rvChatList.adapter = adapter
        FirebaseDatabase.getInstance(BuildConfig.GOOGLE_BASE_URL).getReference("chatRoomList")
            .child(args.chatRoomKey).child("chatList")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    result.clear()
                    for (data in snapshot.children) {
                        val data = data.getValue(Chat::class.java)
                        if (data?.senderEmail == user.email) {
                            result.add(ChatType.SentChat(data))
                        } else {
                            result.add(ChatType.ReceivedChat(data!!))
                        }
                        adapter.notifyDataSetChanged()
                        if (result.size > 0) {
                            binding.rvChatList.scrollToPosition(result.size - 1)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ChatFragment", "데이터를 가져오지 못했습니다.")
                }
            })
    }


}