package com.nik.tkforum.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nik.tkforum.R
import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.databinding.ItemChatRoomBinding

class ChatRoomListAdapter :
    ListAdapter<ChatRoom, ChatRoomListAdapter.ChatListViewHolder>(ChatRoomListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ChatListViewHolder(
        private val biding: ItemChatRoomBinding,
    ) : RecyclerView.ViewHolder(biding.root) {

        fun bind(chatRoom: ChatRoom) {
            val lastChat = chatRoom.chatList.values.toList().lastOrNull() ?: R.string.empty_chat
            biding.tvChatTitle.text = "${chatRoom.createUserName} 님의 채팅방 입니다"
            biding.tvChatUserCount.text = "${chatRoom.userList.size} 명"
            biding.tvLastChat.text = lastChat.toString()
            biding.ivChatThumbnail.load(chatRoom.createUserProfile) {
                transformations(CircleCropTransformation())
            }
        }

        companion object {

            fun from(parent: ViewGroup): ChatListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ChatListViewHolder(
                    ItemChatRoomBinding.inflate(inflater, parent, false),
                )
            }
        }
    }

    private class ChatRoomListDiffUtil : DiffUtil.ItemCallback<ChatRoom>() {

        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }
}