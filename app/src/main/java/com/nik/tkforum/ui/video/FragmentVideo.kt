package com.nik.tkforum.ui.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.databinding.FragmentVideoBinding
import com.nik.tkforum.ui.Constants
import com.nik.tkforum.ui.TekkenForumApplication
import com.nik.tkforum.ui.VideoClickListener
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
}