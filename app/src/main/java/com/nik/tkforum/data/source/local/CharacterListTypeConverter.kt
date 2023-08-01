package com.nik.tkforum.data.source.local

import androidx.room.TypeConverter
import com.nik.tkforum.data.model.CharacterData
import com.squareup.moshi.JsonAdapter

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class CharacterListTypeConverter {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, CharacterData::class.java)
    private val adapter: JsonAdapter<List<CharacterData>> = moshi.adapter(listType)
    @TypeConverter
    fun fromSeasonCharacterList(characterList: List<CharacterData>): String {
        return adapter.toJson(characterList)
    }

    @TypeConverter
    fun toSeasonCharacterList(characterListJson: String?): List<CharacterData>? {
        return adapter.fromJson(characterListJson)
    }
}