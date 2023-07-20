package com.nik.tkforum.ui.frameList

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentFrameListBinding
import com.nik.tkforum.ui.BaseFragment

class FrameListFragment : BaseFragment() {

    override val binding get() = _binding as FragmentFrameListBinding
    override val layoutId: Int get() = R.layout.fragment_frame_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        val frameDataAdapter = FrameListAdapter()
        val frameHeaderAdapter = FrameHeaderAdapter()
        binding.rvFrameDataList.adapter = ConcatAdapter(frameHeaderAdapter, frameDataAdapter)
    }

}