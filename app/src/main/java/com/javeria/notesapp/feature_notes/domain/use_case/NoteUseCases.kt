package com.javeria.notesapp.feature_notes.domain.use_case

//Inject this class into viewmodel
data class NoteUseCases(
    val getNotesUseCases: GetNotesUseCases,
    val deleteNoteUseCase: DeleteNoteUseCase
)