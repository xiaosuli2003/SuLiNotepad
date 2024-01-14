package com.xiaosuli.notepad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    var flag = true

    private val repository = NoteRepository(application)

    fun insertNote(title: String, content: String): Long {
        return repository.insertNote(title, content)
    }

    fun deleteNote(id: Int) {
        repository.deleteNote(id)
    }

    fun updateNote(id: Int, title: String, content: String) {
        repository.updateNote(id, title, content)

    }

    fun queryAllNote(): LiveData<List<Note>> {
        return repository.queryAllNote()
    }

    fun queryAllNote2(): List<Note> {
        return repository.queryAllNote2()
    }

    fun queryOneNoteByIdAndContent(id: Int, content: String, title: String): List<Note> {
        return repository.queryOneNoteByIdAndContent(id, content, title)
    }

    fun queryNoteByKeyword(keyword: String): List<Note> {
        return repository.queryNoteByKeyword(keyword)
    }
}