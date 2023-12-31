package com.nik.tkforum.ui.video

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.nik.tkforum.R
import com.nik.tkforum.databinding.FragmentVideoBinding
import com.nik.tkforum.util.Constants
import com.nik.tkforum.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoFragment : BaseFragment(), VideoClickListener {

    override val binding get() = _binding as FragmentVideoBinding
    override val layoutId: Int get() = R.layout.fragment_video
    private val viewModel: VideoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchVideo()
        val editText = binding.etVideo
        editText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (editText.text.isNullOrEmpty()) {
                    Snackbar.make(
                        binding.root,
                        R.string.is_blank,
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.close_snack_bar) {
                    }.show()
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

    private fun searchVideo() {
        val adapter = VideoAdapter(this)
        var keyword = binding.etVideo.text.toString()
        if (keyword.isEmpty()) {
            keyword = Constants.RECOMMENDED_VIDEO_TAG.random()
        }
        binding.rvVideoItemList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadVideo(keyword).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onClick(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}