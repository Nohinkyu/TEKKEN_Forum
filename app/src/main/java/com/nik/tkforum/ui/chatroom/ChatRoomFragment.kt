package com.nik.tkforum.ui.chatroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentChatRoomBinding
import com.nik.tkforum.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment() {

    override val binding get() = _binding as FragmentChatRoomBinding
    override val layoutId: Int get() = R.layout.fragment_chat_room

    private val args: ChatRoomFragmentArgs by navArgs()

    private val viewModel: ChatRoomViewModel by viewModels()

    private val adapter = ChatListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        binding.chatRoomTitle = args.chatRoomHostName

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
        binding.rvChatList.apply {
            addOnLayoutChangeListener(onLayoutChangeListener)
        }
        deleteChatRoom()
        setErrorMessage()
        sendErrorMessage()
        setDeleteFailMessage()
        viewModel.loadChatList(args.chatRoomKey)
        viewModel.addChatListener(args.chatRoomKey)
        setChatList()
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvChatList.layoutManager?.scrollToPosition(adapter.currentList.size - 1)
            }
        })
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.rvChatList.scrollBy(0, oldBottom - bottom)
            }
        }

    private fun sendChat() {
        viewModel.sendChat(args.chatRoomKey, binding.etChat.text.toString())
    }

    private fun sendErrorMessage() {
        lifecycleScope.launch {
            viewModel.sendError.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { sendError ->
                if (sendError) {
                    Snackbar.make(binding.root, R.string.send_error_message, Snackbar.LENGTH_LONG)
                        .setAction(R.string.close_snack_bar) {
                        }.show()
                }
            }
        }
    }

    private fun setErrorMessage() {
        lifecycleScope.launch {
            viewModel.isLoading.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
                .collect { isError ->
                    if (isError == false) {
                        Snackbar.make(
                            binding.root,
                            R.string.network_error_message,
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.close_snack_bar) {
                        }.show()
                    }
                }
        }
    }

    private fun deleteChatRoom() {
        binding.ibDeleteChatRoom.setOnClickListener {
            showDeleteChatRoomDialog()
        }
    }

    private fun setDeleteFailMessage() {
        lifecycleScope.launch {
            viewModel.isChatRoomHost.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isChatRoomHost ->
                if (isChatRoomHost) {
                    Snackbar.make(
                        binding.root,
                        R.string.not_host,
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.close_snack_bar) {
                    }.show()
                }
            }
        }
    }

    private fun setChatList() {
        lifecycleScope.launch {
            viewModel.chatList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { ChatList ->
                    val chatTypeList = mutableListOf<ChatType>()
                    for (chat in ChatList) {
                        if (chat.senderEmail == viewModel.userInfo.email) {
                            chatTypeList.add(ChatType.SentChat(chat))
                        } else {
                            chatTypeList.add(ChatType.ReceivedChat(chat))
                        }
                    }
                    adapter.submitList(chatTypeList)
                }
        }
    }

    private fun showDeleteChatRoomDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        val action = ChatRoomFragmentDirections.actionNavChatRoomToNavChat()

        builder.setTitle(R.string.dialog_title_delete_chat_room)
        builder.setMessage(R.string.dialog_title_delete_chat_room)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            viewModel.deleteChatRoom(args.chatRoomKey, args.chatRoomHostName)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.dialog_negative) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}