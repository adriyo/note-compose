package com.adriyo.notecompose.data.local.room.dao

import androidx.room.*
import com.adriyo.notecompose.data.local.room.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * from note")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * from note WHERE id = :id")
    suspend fun findById(id: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

}