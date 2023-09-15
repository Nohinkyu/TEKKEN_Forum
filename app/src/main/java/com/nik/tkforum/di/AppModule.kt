package com.nik.tkforum.di

import android.content.Context
import androidx.room.Room
import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.data.source.local.CharacterListDao
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            name = Constants.CHARACTER_DATA_BASE
        ).build()
    }

    @Provides
    fun provideCharacterListDao(appDatabase: AppDatabase): CharacterListDao {
        return appDatabase.CharacterListDao()
    }

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }
}