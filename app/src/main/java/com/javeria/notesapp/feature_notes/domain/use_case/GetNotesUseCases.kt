package com.javeria.notesapp.feature_notes.domain.use_case

import com.javeria.notesapp.feature_notes.domain.model.Note
import com.javeria.notesapp.feature_notes.domain.repository.NoteRepository
import com.javeria.notesapp.feature_notes.domain.util.NoteOrder
import com.javeria.notesapp.feature_notes.domain.util.OrderedType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCases(private val repository: NoteRepository) {

    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Date(OrderedType.Descending)): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderedType) {
                is OrderedType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Color -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Title -> notes.sortedBy { it.color }
                    }
                }

                is OrderedType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Color -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Title -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}