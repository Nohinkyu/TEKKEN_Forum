package com.nik.tkforum.ui.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import coil.transform.CircleCropTransformation
import com.nik.tkforum.databinding.ItemReceivedChatBinding
import com.nik.tkforum.databinding.ItemSentChatBinding

private const val ITEM_TYPE_RECEIVED_CHAT = 0
private const val ITEM_TYPE_SENT_CHAT = 1

class ChatListAdapter : ListAdapter<ChatType, ViewHolder>(ChatTypeDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatType.ReceivedChat -> ITEM_TYPE_RECEIVED_CHAT
            is ChatType.SentChat -> ITEM_TYPE_SENT_CHAT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TYPE_RECEIVED_CHAT -> ReceivedChatViewHolder.from(parent)
            ITEM_TYPE_SENT_CHAT -> SentChatViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ReceivedChatViewHolder -> {
                val item = getItem(position) as ChatType.ReceivedChat
                holder.bind(item)
            }

            is SentChatViewHolder -> {
                val item = getItem(position) as ChatType.SentChat
                holder.bind(item)
            }
        }
    }

    class ReceivedChatViewHolder(val binding: ItemReceivedChatBinding) :
        ViewHolder(binding.root) {

        fun bind(item: ChatType.ReceivedChat) {
            binding.tvRecivedChat.text = item.chat.message
            binding.tvReceivedChatNickname.text = item.chat.senderNickname
            binding.ivReceivedChatProfile.load(item.chat.profileImage) {
                transformations(CircleCropTransformation())
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): ReceivedChatViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ReceivedChatViewHolder(
                    ItemReceivedChatBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    class SentChatViewHolder(val binding: ItemSentChatBinding) :
        ViewHolder(binding.root) {

        fun bind(item: ChatType.SentChat) {
            binding.tvSentChat.text = item.chat.message
            binding.tvSentChatNickname.text = item.chat.senderNickname
            binding.ivSentChatProfile.load(item.chat.profileImage) {
                transformations(CircleCropTransformation())
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): SentChatViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return SentChatViewHolder(
                    ItemSentChatBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    class ChatTypeDiffUtil : DiffUtil.ItemCallback<ChatType>() {

        override fun areItemsTheSame(oldItem: ChatType, newItem: ChatType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatType, newItem: ChatType): Boolean {
            return oldItem == newItem
        }
    }
}
