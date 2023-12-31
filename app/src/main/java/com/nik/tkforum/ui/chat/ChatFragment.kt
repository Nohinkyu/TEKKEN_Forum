package com.nik.tkforum.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentChatBinding
import com.nik.tkforum.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment(), ChatRoomClickListener {

    override val binding get() = _binding as FragmentChatBinding
    override val layoutId: Int get() = R.layout.fragment_chat

    private val viewModel: ChatRoomListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        binding.tbChat.setOnMenuItemClickListener { menuItme ->
            when (menuItme.itemId) {
                R.id.action_add_chat_room -> {
                    showCreateChatRoomDialog()
                    true
                }

                else -> false
            }
        }
    }

    private fun createChatRoom() {
        viewModel.checkChatRoom()
        if (viewModel.isChatRoom.isBlank()) {
            viewModel.createChatRoom()
            viewModel.getCreateChatRoomKey()
            lifecycleScope.launch {
                viewModel.isGetChatRoomKey.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
                    .collect { isGetChatRoomKey ->
                        if (isGetChatRoomKey) {
                            findNavController().navigate(
                                ChatFragmentDirections.actionNavChatToNavChatRoom(
                                    viewModel.lastChatRoomKey.value, viewModel.userInfo.nickname
                                )
                            )
                        }
                    }
            }
        } else if (viewModel.isChatRoom.isNotBlank()) {
            Snackbar.make(
                binding.root,
                R.string.is_chat_room_message,
                Snackbar.LENGTH_LONG
            ).setAction(R.string.close_snack_bar) {
            }.show()
        }
    }

    private fun setLayout() {
        setErrorMessage()
        val adapter = ChatRoomListAdapter(this)
        binding.rvChatRoomList.adapter = adapter
        viewModel.loadAllChatRoom()
        lifecycleScope.launch {
            viewModel.chatRoomList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
                .collect { chatRoomList -> adapter.submitList(chatRoomList) }
        }
    }

    override fun chatRoomClick(chatRoomKey: String, hostName: String) {
        val action = ChatFragmentDirections.actionNavChatToNavChatRoom(chatRoomKey, hostName)
        findNavController().navigate(action)
        viewModel.joinUser(chatRoomKey)
    }

    private fun setErrorMessage() {
        lifecycleScope.launch {
            viewModel.isError.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
                .collect { isError ->
                    if (isError) {
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

    private fun showCreateChatRoomDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)

        builder.setTitle(R.string.dialog_title_create_chat_room)
        builder.setMessage(R.string.dialog_message_create_chat_room)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            createChatRoom()
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.dialog_negative) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
