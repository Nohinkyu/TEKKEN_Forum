package com.nik.tkforum.ui.frameList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.navArgs
import com.nik.tkforum.R
import com.nik.tkforum.data.model.FrameData
import com.nik.tkforum.databinding.FragmentFrameListBinding
import com.nik.tkforum.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FrameListFragment : BaseFragment() {

    override val binding get() = _binding as FragmentFrameListBinding
    override val layoutId: Int get() = R.layout.fragment_frame_list

    private val args: FrameListFragmentArgs by navArgs()

    private val frameDataAdapter = FrameListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        binding.tbFrameList.title = args.characterData.characterName
    }

    private fun setLayout() {
        setTextWatcher()
        val frameData = args.characterData.moveList?.values?.toMutableList()
        frameDataAdapter.submitList(frameData)
        binding.rvFrameDataList.adapter = frameDataAdapter
    }

    private fun setTextWatcher() {
        binding.etFrameList.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = binding.etFrameList.text
                filterList(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun filterList(query: String?) {
        val frameData = args.characterData.moveList?.values?.toMutableList()
        if (query != null && frameData != null) {
            val filteredList = mutableListOf<FrameData>()
            for (data in frameData) {
                if (data.toString().lowercase().contains(query)) {
                    filteredList.add(data)
                }
            }
            frameDataAdapter.submitList(filteredList)
        }
    }
}