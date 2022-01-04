package com.adriyo.notecompose.data.local.room.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adriyo.notecompose.data.local.room.dao.NoteDao
import com.adriyo.notecompose.data.local.room.entities.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
