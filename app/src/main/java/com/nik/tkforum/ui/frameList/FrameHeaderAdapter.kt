package com.nik.tkforum.ui.frameList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.tkforum.databinding.ItemFrameHeaderBinding

class FrameHeaderAdapter() : RecyclerView.Adapter<FrameHeaderAdapter.FrameHeaderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameHeaderViewHolder {
        return FrameHeaderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FrameHeaderViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }


    class FrameHeaderViewHolder(private val binding: ItemFrameHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun from(parent: ViewGroup): FrameHeaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return FrameHeaderViewHolder(
                    ItemFrameHeaderBinding.inflate(inflater, parent, false),
                )
            }
        }
    }
}