package com.nik.tkforum.ui.home

import com.nik.tkforum.data.model.CharacterData

interface CharacterClickListener {

    fun characterClick(characterData: CharacterData)
}