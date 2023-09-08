package com.nik.tkforum.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [CharacterListEntity::class], version = 1)
@TypeConverters(CharacterListTypeConverter::class)
abstract class  AppDatabase:RoomDatabase() {

    abstract fun CharacterListDao(): CharacterListDao
}