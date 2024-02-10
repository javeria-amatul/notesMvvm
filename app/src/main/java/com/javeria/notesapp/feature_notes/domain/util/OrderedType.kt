package com.javeria.notesapp.feature_notes.domain.util

sealed class OrderedType {
    object Ascending: OrderedType()
    object Descending: OrderedType()
}