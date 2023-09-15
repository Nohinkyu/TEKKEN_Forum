package com.nik.tkforum.ui.frameList

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentFrameListBinding
import com.nik.tkforum.ui.BaseFragment

class FrameListFragment : BaseFragment() {

    override val binding get() = _binding as FragmentFrameListBinding
    override val layoutId: Int get() = R.layout.fragment_frame_list

    private val args: FrameListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        binding.tbFrameList.title = args.characterData.characterName
    }

    private fun setLayout() {
        val frameDataAdapter = FrameListAdapter()
        val frameHeaderAdapter = FrameHeaderAdapter()
        frameDataAdapter.submitList(args.characterData.moveList?.values?.toList())
        binding.rvFrameDataList.adapter = ConcatAdapter(frameHeaderAdapter, frameDataAdapter)
    }

}