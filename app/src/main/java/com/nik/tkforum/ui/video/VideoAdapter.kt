package com.nik.tkforum.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nik.tkforum.databinding.ItemVideoBinding
import com.nik.tkforum.data.model.Video
import com.nik.tkforum.util.VideoClickListener

class VideoAdapter(private val clickListener: VideoClickListener) :
    PagingDataAdapter<Video, VideoAdapter.VideoViewHolder>(VideoListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, clickListener) }
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video, clickListener: VideoClickListener) {
            binding.video = video
            binding.clickListener = clickListener
        }

        companion object {

            fun from(parent: ViewGroup): VideoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return VideoViewHolder(
                    ItemVideoBinding.inflate(inflater, parent, false),
                )
            }
        }
    }

    private class VideoListDiffUtil : DiffUtil.ItemCallback<Video>() {

        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }
}