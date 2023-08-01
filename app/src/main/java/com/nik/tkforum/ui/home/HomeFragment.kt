package com.nik.tkforum.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.room.Room
import com.nik.tkforum.R
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.data.repository.CharacterListRepository
import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.databinding.FragmentHomeBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    override val binding get() = _binding as FragmentHomeBinding
    override val layoutId: Int get() = R.layout.fragment_home
    private val adapter = CharacterListAdapter()

    private val viewModel by viewModels<CharacterListViewModel> {
        CharacterListViewModel.provideFactory(
            repository = CharacterListRepository(TekkenForumApplication.database)
        )
    }

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
}