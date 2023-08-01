package com.nik.tkforum.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nik.tkforum.databinding.ItemCharacterBinding
import com.nik.tkforum.databinding.ItemHeaderCharacterListBinding

private const val ITEM_TYPE_HEADER = 0
private const val ITEM_TYPE_CHARACTER = 1

class CharacterListAdapter(private val clickListener: CharacterClickListener) :
    RecyclerView.Adapter<ViewHolder>() {

    private val characterList = mutableListOf<CharacterListSection>()

    override fun getItemViewType(position: Int): Int {
        return when (characterList[position]) {
            is CharacterListSection.CharacterListHeader -> ITEM_TYPE_HEADER
            is CharacterListSection.Character -> ITEM_TYPE_CHARACTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_TYPE_CHARACTER -> CharacterViewHolder.from(parent)
            else -> throw ClassCastException("Unknown ViewType $viewType")
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val item = characterList[position] as CharacterListSection.CharacterListHeader
                holder.bind(item)
            }

            is CharacterViewHolder -> {
                val item = characterList[position] as CharacterListSection.Character
                holder.bind(item, clickListener)
            }
        }
    }


    override fun getItemCount(): Int {
        return characterList.size
    }

    fun submitList(list: List<CharacterListSection>) {
        characterList.clear()
        characterList.addAll(list)
        notifyDataSetChanged()
    }

    class HeaderViewHolder(val binding: ItemHeaderCharacterListBinding) : ViewHolder(binding.root) {

        fun bind(characterData: CharacterListSection.CharacterListHeader) {
            binding.season = characterData.season
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): HeaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return HeaderViewHolder(
                    ItemHeaderCharacterListBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    class CharacterViewHolder(val binding: ItemCharacterBinding) : ViewHolder(binding.root) {

        fun bind(
            characterData: CharacterListSection.Character,
            clickListener: CharacterClickListener
        ) {
            binding.characterInfo = characterData.character
            binding.clickListener = clickListener
        }

        companion object {
            fun from(
                parent: ViewGroup,
            ): CharacterViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return CharacterViewHolder(
                    ItemCharacterBinding.inflate(inflater, parent, false)
                )
            }
        }
    }
}