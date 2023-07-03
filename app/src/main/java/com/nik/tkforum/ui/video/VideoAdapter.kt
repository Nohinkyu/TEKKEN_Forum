package com.nik.tkforum.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nik.tkforum.databinding.ItemVideoBinding
import com.nik.tkforum.util.DateFormat.convertToDisplayDate
import com.nik.tkforum.data.Video
import com.nik.tkforum.util.VideoClickListener

class VideoAdapter(private val clickListener: VideoClickListener) :
    ListAdapter<Video, VideoAdapter.VideoViewHolder>(VideoListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position], clickListener)
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video, clickListener: VideoClickListener) {
            binding.video = video
            binding.clickListner = clickListener
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