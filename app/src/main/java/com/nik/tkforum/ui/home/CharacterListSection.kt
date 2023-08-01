package com.nik.tkforum.ui.home

import com.nik.tkforum.data.model.CharacterData

const val CHARACTER_LIST_SEASON = 0
const val CHARACTER_LIST_CHARACTER = 1

sealed class CharacterListSection {

    data class CharacterListHeader(val season: String) : CharacterListSection() {

        override val id: Int
            get() = CHARACTER_LIST_SEASON
    }

    data class Character(val character: CharacterData) : CharacterListSection() {

        override val id: Int
            get() = CHARACTER_LIST_CHARACTER
    }

    abstract val id: Int
}