package com.nik.tkforum.ui.video

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nik.tkforum.databinding.FragmentVideoBinding
import com.nik.tkforum.repository.VideoRepository
import com.nik.tkforum.util.Constants
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.util.VideoClickListener

class FragmentVideo : Fragment(), VideoClickListener {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<VideoViewModel> {
        VideoViewModel.provideFactory(repository = VideoRepository(TekkenForumApplication.appContainer.provideApiClient()))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchVideo()
        val editText = binding.etVideo
        editText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editText.text.isNullOrEmpty()) {
                    return@setOnKeyListener true
                }
                val imm =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etVideo.windowToken, 0)
                searchVideo()
                editText.text = null
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchVideo() {
        val adapter = VideoAdapter(this)
        var keyword = binding.etVideo.text.toString()
        if (keyword.isEmpty()) {
            keyword = Constants.RECOMMENDED_VIDEO_TAG.random()
        }
        binding.rvVideoItemList.adapter = adapter
        viewModel.loadVideo(keyword)
        viewModel.videoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onClick(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}