package com.adriyo.notecompose.feature.note

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.notecompose.shared.components.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        TextFieldState(
            hint = "Title"
        )
    )
    val noteTitle = _noteTitle

    private val _noteContent = mutableStateOf(
        TextFieldState(
            hint = "Content"
        )
    )
    val noteContent = _noteContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                currentNoteId = noteId
                viewModelScope.launch {
                    repository.getNoteById(noteId)?.also { note ->
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.note,
                        )
                    }
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            repository.saveNote(currentNoteId, noteTitle.value.text, noteContent.value.text)
            _eventFlow.emit(UiEvent.SaveNote)
        }
    }

    fun onTitleValueChange(text: String) {
        _noteTitle.value = noteTitle.value.copy(
            text = text
        )
    }

    fun onNoteContentValueChange(text: String) {
        _noteContent.value = noteContent.value.copy(
            text = text
        )
    }

}

sealed class UiEvent {
    object SaveNote : UiEvent()
}