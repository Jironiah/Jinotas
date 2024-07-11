package com.example.jinotas.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {
    @Query("SELECT * FROM notes")
    fun getNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): Note

    @Query("SELECT * FROM notes WHERE title = :title")
    fun getNoteByTitle(title: String): Note

    @Query("SELECT COUNT(*) FROM notes")
    fun getNotesCount(): Int

    @Insert
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}