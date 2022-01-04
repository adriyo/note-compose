package com.adriyo.notecompose.di

import android.content.Context
import androidx.room.Room
import com.adriyo.notecompose.data.local.room.config.AppDatabase
import com.adriyo.notecompose.data.repositories.NoteRepositoryImpl
import com.adriyo.notecompose.feature.note.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteRepository(database: AppDatabase): NoteRepository {
        return NoteRepositoryImpl(dataProvider = database.noteDao())
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "notes_db"
        ).build()
    }


}