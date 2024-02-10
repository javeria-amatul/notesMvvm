package com.javeria.notesapp.feature_notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.javeria.notesapp.ui.theme.BabyBlue
import com.javeria.notesapp.ui.theme.LightGreen
import com.javeria.notesapp.ui.theme.RedOrange
import com.javeria.notesapp.ui.theme.RedPink
import com.javeria.notesapp.ui.theme.Violet

@Entity
data class Note(
    val title: String, val content: String, val timestamp
    : Long, val color: Int, @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}