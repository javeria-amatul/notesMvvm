package com.javeria.notesapp.feature_notes.presentation.notes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javeria.notesapp.feature_notes.domain.model.Note
import com.javeria.notesapp.feature_notes.domain.use_case.NoteUseCases
import com.javeria.notesapp.feature_notes.domain.util.NoteOrder
import com.javeria.notesapp.feature_notes.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteUseCases: NoteUseCases) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state = _state
    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(events: NotesEvents) {
        when (events) {
            is NotesEvents.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(events.note)
                    recentlyDeletedNote = events.note
                }
            }

            is NotesEvents.Order -> {
                if (state.value.noteOrder::class == events.noteOrder::class &&
                    state.value.noteOrder.orderType == events.noteOrder.orderType
                ) {
                    return
                }

                getNotes(events.noteOrder)
            }

            NotesEvents.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            NotesEvents.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        //getNotesJob is required as every time getNotes() is called it creates a new coroutine.
        //getNotesUseCases observes on the db changes
        // In order to cancel the old coroutine started by getNotesUseCases we use getNotesJob
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCases(noteOrder).onEach { note ->
            _state.value = state.value.copy(
                notes = note,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}