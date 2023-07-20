package com.nik.tkforum.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentHomeBinding
import com.nik.tkforum.ui.BaseFragment

class HomeFragment : BaseFragment() {

    override val binding get() = _binding as FragmentHomeBinding
    override val layoutId: Int get() = R.layout.fragment_home
    private val adapter = CharacterListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

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
}