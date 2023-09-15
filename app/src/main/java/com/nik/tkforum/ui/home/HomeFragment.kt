package com.nik.tkforum.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.data.model.CharacterData
import com.nik.tkforum.data.source.remote.network.FirebaseData
import com.nik.tkforum.data.source.remote.network.FirebaseData.getUserToken
import com.nik.tkforum.data.source.remote.network.FirebaseData.setUserInfo
import com.nik.tkforum.databinding.FragmentHomeBinding
import com.nik.tkforum.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment(), CharacterClickListener {

    override val binding get() = _binding as FragmentHomeBinding
    override val layoutId: Int get() = R.layout.fragment_home
    private val adapter = CharacterListAdapter(this)

    private val viewModel: CharacterListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setCharacterList()
        isSavedFrameData()
        isSuccessSave()
        setErrorMessage()
        setFirebaseUserInfo()

        binding.ibEight.setOnClickListener {
            setEightCharacterList()
        }

        binding.ibSeven.setOnClickListener {
            setSevenCharacterList()
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)

        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == CHARACTER_LIST_SEASON) {
                    gridLayoutManager.spanCount
                } else 1
            }
        }
        binding.rvCharacterList.layoutManager = gridLayoutManager
    }

    private fun setSevenCharacterList() {
        binding.rvCharacterList.adapter = adapter
        lifecycleScope.launch {
            viewModel.loadSevenCharacterList()
            viewModel.characterList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { characterList -> adapter.submitList(characterList) }
        }
    }

    private fun setEightCharacterList() {
        binding.rvCharacterList.adapter = adapter
        lifecycleScope.launch {
            viewModel.loadEightCharacterList()
            viewModel.characterList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { characterList -> adapter.submitList(characterList) }
        }
    }

    private fun setCharacterList() {
        if (viewModel.isSeriesSwitchCheck.isNotBlank()) {
            setEightCharacterList()
        } else {
            setSevenCharacterList()
        }
    }

    override fun characterClick(characterData: CharacterData) {
        val action = HomeFragmentDirections.actionNavHomeToNavFrameList(characterData)
        if (characterData.moveList != null) {
            findNavController().navigate(action)
        } else {
            Snackbar.make(
                binding.root,
                R.string.empty_data,
                Snackbar.LENGTH_LONG
            ).setAction(R.string.close_snack_bar) {
            }.show()
        }
    }

    private fun isSavedFrameData() {
        viewModel.isSavedFrameData()
        lifecycleScope.launch {
            viewModel.isSavedFrameData.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isEmpty ->
                if (!isEmpty) {
                    getFrameDataDialog()
                }
            }
        }
    }

    private fun isSuccessSave() {
        lifecycleScope.launch {
            viewModel.isCharacterSave.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isSuccess ->
                if (isSuccess) {
                    getFrameDataSuccessDialog()
                }
            }
        }
    }

    private fun getFrameDataDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)

        builder.setTitle(R.string.dialog_ge_frame_data_title)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            dialog.dismiss()
            setProgressDialog()
            viewModel.getCharacterList()
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getFrameDataSuccessDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)

        builder.setTitle(R.string.dialog_success_get_frame_data_title)
        builder.setPositiveButton(R.string.dialog_positive) { dialog, _ ->
            dialog.dismiss()
            setSevenCharacterList()
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun setProgressDialog() {
        val action = HomeFragmentDirections.actionNavHomeToProgressDialog()
        lifecycleScope.launch {
            viewModel.isLoading.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isLoading ->
                if (isLoading == true) {
                    findNavController().navigate(action)
                } else {
                    findNavController().navigateUp()
                }
            }
        }
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

    private fun setFirebaseUserInfo() {
        if (FirebaseData.token.isNullOrBlank()) {
            setUserInfo()
            getUserToken()
        }
    }
}