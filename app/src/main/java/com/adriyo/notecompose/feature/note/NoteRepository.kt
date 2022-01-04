package com.adriyo.notecompose.feature.note

import com.adriyo.notecompose.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNote(id: Int?, title: String, note: String)

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(noteId: Int): Note?
    suspend fun deleteNote(note: Note)

}