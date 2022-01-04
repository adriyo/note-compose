package com.adriyo.notecompose.data.model

import com.adriyo.notecompose.data.local.room.entities.NoteEntity

data class Note(
    var id: Int,
    var localId: Int?,
    var title: String,
    var note: String,
    var createdAt: Long,
    var updatedAt: Long,
    var isSelected: Boolean = false
) {
    fun toEntity(): NoteEntity {
        return NoteEntity(
            id = localId,
            title = title,
            note = note,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}