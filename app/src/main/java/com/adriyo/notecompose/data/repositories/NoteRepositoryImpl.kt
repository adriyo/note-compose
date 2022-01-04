package com.adriyo.notecompose.data.repositories

import com.adriyo.notecompose.data.local.room.dao.NoteDao
import com.adriyo.notecompose.data.local.room.entities.NoteEntity
import com.adriyo.notecompose.data.model.Note
import com.adriyo.notecompose.feature.note.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dataProvider: NoteDao
) : NoteRepository {

    override suspend fun saveNote(id: Int?, title: String, note: String) {
        if (id == null) {
            dataProvider.insertNote(
                NoteEntity(
                    id = id,
                    title = title,
                    note = note,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
            return
        }
        val result = getNoteById(id) ?: return
        dataProvider.updateNote(
            result.toEntity().copy(
                title = title,
                note = note,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getNotes(): Flow<List<Note>> = flow {
        dataProvider.getNotes().collect {
            emit(it.map { note -> note.toNote() })
        }
    }

    override suspend fun getNoteById(noteId: Int): Note? {
        return dataProvider.findById(noteId)?.toNote()
    }

    override suspend fun deleteNote(note: Note) {
        dataProvider.delete(note.toEntity())
    }
}