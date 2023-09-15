package com.nik.tkforum.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nik.tkforum.util.Constants


@Database(entities = [CharacterListEntity::class], version = 1)
@TypeConverters(CharacterListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun CharacterListDao(): CharacterListDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                name = Constants.CHARACTER_DATA_BASE
            ).build().also {
                instance = it
            }
        }
    }
}