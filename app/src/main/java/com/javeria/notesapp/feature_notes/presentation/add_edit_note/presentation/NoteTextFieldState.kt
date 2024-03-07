package com.javeria.notesapp.feature_notes.presentation.add_edit_note.presentation

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)