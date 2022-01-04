package com.adriyo.notecompose.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adriyo.notecompose.data.model.Note

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey var id: Int? = null,
    @ColumnInfo(name = "remoteId") var remoteId: Int = -1,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "note") var note: String? = null,
    @ColumnInfo(name = "created_at") var createdAt: Long,
    @ColumnInfo(name = "updated_at") var updatedAt: Long,
) {
    fun toNote(): Note {
        return Note(
            id = remoteId,
            localId = id,
            title = title ?: "",
            note = note ?: "",
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}