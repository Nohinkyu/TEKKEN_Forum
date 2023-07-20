package com.nik.tkforum.ui.frameList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nik.tkforum.data.FrameData
import com.nik.tkforum.databinding.ItemFrameDataBinding

class FrameListAdapter() :
    ListAdapter<FrameData, FrameListAdapter.FrameDataViewHolder>(FrameDataDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameDataViewHolder {
        return FrameDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FrameDataViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    class FrameDataViewHolder(private val binding: ItemFrameDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(frameData: FrameData) {
            binding.tvMoveName.text = frameData.name
            binding.tvMoveCommand.text = frameData.numberCommand
            binding.tvMoveStart.text = frameData.startUpFrame
            binding.tvMoveGrad.text = frameData.gardFrame
            binding.tvMoveHit.text = frameData.hitFrame
            binding.tvMoveCounter.text = frameData.counterFrame
            binding.tvMoveHitLevel.text = frameData.hitLevel
            binding.tvMoveDamage.text = frameData.damage
        }

        companion object {

            fun from(parent: ViewGroup): FrameDataViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return FrameDataViewHolder(
                    ItemFrameDataBinding.inflate(inflater, parent, false),
                )
            }
        }
    }

    private class FrameDataDiffUtil : DiffUtil.ItemCallback<FrameData>() {

        override fun areItemsTheSame(oldItem: FrameData, newItem: FrameData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FrameData, newItem: FrameData): Boolean {
            return oldItem == newItem
        }
    }
}