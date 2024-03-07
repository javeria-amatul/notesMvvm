package com.javeria.notesapp.feature_notes.presentation.notes

import com.javeria.notesapp.feature_notes.domain.model.Note
import com.javeria.notesapp.feature_notes.domain.util.NoteOrder

sealed class NotesEvents {
    data class Order(val noteOrder: NoteOrder) : NotesEvents()
    data class DeleteNote(val note: Note) : NotesEvents()
    data object RestoreNote : NotesEvents()
    data object ToggleOrderSection : NotesEvents()
}