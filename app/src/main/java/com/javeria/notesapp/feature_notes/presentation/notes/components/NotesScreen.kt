package com.javeria.notesapp.feature_notes.presentation.notes.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.javeria.notesapp.feature_notes.domain.model.Note
import com.javeria.notesapp.feature_notes.presentation.Screen
import com.javeria.notesapp.feature_notes.presentation.notes.NotesEvents
import com.javeria.notesapp.feature_notes.presentation.notes.NotesViewModel
import com.javeria.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(navController: NavController, viewModel: NotesViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val scaffoldState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                }, containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },

    ) {
        it.calculateBottomPadding()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your note", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { viewModel.onEvent(NotesEvents.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")

                    AnimatedVisibility(
                        visible = state.isOrderSectionVisible,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        OrderSection(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                            noteOrder = state.noteOrder,
                            onOrderChange = {
                                viewModel.onEvent(NotesEvents.Order(it))
                            })

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(state.notes) { note: Note ->
                            NoteItem(note = note,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                                    },
                                onDeleteClick = {
                                    viewModel.onEvent(NotesEvents.DeleteNote(note))
                                    scope.launch {
                                        val result =
                                            scaffoldState.showSnackbar(
                                                "Note Deleted", "Undo"
                                            )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(NotesEvents.RestoreNote)
                                        }
                                    }
                                })
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesAppTheme {
        NotesScreen(navController = rememberNavController())
    }
}