package com.adriyo.notecompose.feature.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriyo.notecompose.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notesState = MutableStateFlow<List<Note>>(emptyList())

    val notesState: StateFlow<List<Note>>
        get() = _notesState

    private val _selectedNotesState = MutableStateFlow<List<Boolean>>(emptyList())
    val selectedNotesState: StateFlow<List<Boolean>>
        get() = _selectedNotesState

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            repository.getNotes().collect {
                _notesState.value = it
            }
        }
    }

    fun updateSelectedItem(note: Note) {
        viewModelScope.launch {
            _notesState.emit(_notesState.value.map {
                if (it.localId == note.localId) {
                    it.copy(isSelected = !it.isSelected)
                } else it
            })
            collectSelectedNotesFlow()
        }
    }

    fun resetSelectedItems() {
        viewModelScope.launch {
            _notesState.emit(_notesState.value.map {
                it.copy(isSelected = false)
            })
            collectSelectedNotesFlow()
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            val selectedItems = _notesState.value.filter { it.isSelected }
            selectedItems.forEach {
                repository.deleteNote(it)
            }
            resetSelectedItems()
            getNotes()
        }
    }

    private fun collectSelectedNotesFlow() {
        viewModelScope.launch {
            _selectedNotesState.emit(
                _notesState.value.map { it.isSelected }.filter { it }
            )
        }
    }

}