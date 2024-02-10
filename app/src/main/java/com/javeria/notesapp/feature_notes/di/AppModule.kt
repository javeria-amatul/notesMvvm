package com.javeria.notesapp.feature_notes.di

import android.app.Application
import androidx.room.Room
import com.javeria.notesapp.feature_notes.data.data_source.NoteDao
import com.javeria.notesapp.feature_notes.data.data_source.NoteDatabase
import com.javeria.notesapp.feature_notes.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import com.javeria.notesapp.feature_notes.data.repository.NoteRepositoryImpl
import com.javeria.notesapp.feature_notes.domain.repository.NoteRepository
import com.javeria.notesapp.feature_notes.domain.use_case.DeleteNoteUseCase
import com.javeria.notesapp.feature_notes.domain.use_case.GetNotesUseCases
import com.javeria.notesapp.feature_notes.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//One module per feature. Can have one di folder in each module
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(database: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(database.noteDao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCases = GetNotesUseCases(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository))
    }
}