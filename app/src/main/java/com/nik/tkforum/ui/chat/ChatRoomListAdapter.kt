package com.nik.tkforum.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nik.tkforum.R
import com.nik.tkforum.data.ChatRoomInfo
import com.nik.tkforum.databinding.ItemChatRoomBinding
import com.nik.tkforum.util.ChatRoomClickListener

class ChatRoomListAdapter(private val clickListener: ChatRoomClickListener) :
    ListAdapter<ChatRoomInfo, ChatRoomListAdapter.ChatListViewHolder>(ChatRoomListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(currentList[position], clickListener)
    }

    class ChatListViewHolder(
        private val biding: ItemChatRoomBinding,
    ) : RecyclerView.ViewHolder(biding.root) {

        fun bind(chatRoom: ChatRoomInfo, clickListener: ChatRoomClickListener) {
            itemView.setOnClickListener {
                clickListener.chatRoomClick(chatRoom.key)
            }
            val lastChat = chatRoom.chatRoom.chatList.values.toList().lastOrNull()?.message
                ?: itemView.context.getString(R.string.empty_chat)
            biding.tvChatTitle.text = "${chatRoom.chatRoom.createUserName} 님의 채팅방 입니다"
            biding.tvChatUserCount.text = "${chatRoom.chatRoom.userList.size} 명"
            biding.tvLastChat.text = lastChat
            biding.ivChatThumbnail.load(chatRoom.chatRoom.createUserProfile) {
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

    private class ChatRoomListDiffUtil : DiffUtil.ItemCallback<ChatRoomInfo>() {

        override fun areItemsTheSame(oldItem: ChatRoomInfo, newItem: ChatRoomInfo): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: ChatRoomInfo, newItem: ChatRoomInfo): Boolean {
            return oldItem == newItem
        }
    }
}