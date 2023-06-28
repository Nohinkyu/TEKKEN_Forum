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
import androidx.lifecycle.lifecycleScope
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.databinding.FragmentVideoBinding
import com.nik.tkforum.util.Constants
import com.nik.tkforum.ui.TekkenForumApplication
import com.nik.tkforum.util.VideoClickListener
import kotlinx.coroutines.launch

class FragmentVideo : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val videoClickListener = object : VideoClickListener {
        override fun onClick(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
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
        setLayout()
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

    private fun setLayout() {
        val adapter = VideoAdapter(videoClickListener)
        binding.rvVideoItemList.adapter = adapter
        val appContainer = (activity?.application as TekkenForumApplication).appContainer
        val apiClient = appContainer.provideApiClient()
        lifecycleScope.launch {
            val response = apiClient.getVideo(
                BuildConfig.KAKAO_API_KEY,
                Constants.RECOMMENDED_VIDEO_TAG.random()
            )
            adapter.submitList(response.documents)
        }
    }

    private fun searchVideo() {
        val adapter = VideoAdapter(videoClickListener)
        binding.rvVideoItemList.adapter = adapter
        val keyword = binding.etVideo.text.toString()
        val appContainer = (activity?.application as TekkenForumApplication).appContainer
        val apiClient = appContainer.provideApiClient()
        lifecycleScope.launch {
            val response = apiClient.getVideo(
                BuildConfig.KAKAO_API_KEY,
                keyword
            )
            adapter.submitList(response.documents)
        }
    }
}