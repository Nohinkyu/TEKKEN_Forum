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
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.data.model.CharacterData
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
        setSevenCharacterList()

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
            if (!viewModel.isSevenLoad.value) {
                viewModel.loadSevenCharacterList()
                viewModel.characterList.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { characterList -> adapter.submitList(characterList) }
            }
        }
    }

    private fun setEightCharacterList() {
        binding.rvCharacterList.adapter = adapter
        lifecycleScope.launch {
            if (!viewModel.isEightLoad.value) {
                viewModel.loadEightCharacterList()
                viewModel.characterList.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { characterList -> adapter.submitList(characterList) }
            }
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
            ).show()
        }
    }
}