package com.xiaosuli.notepad

import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class NoteRepository {

    private val noteDao = AppDataBase.getDataBase().noteDao()
    private var dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA)
    private var date = Date(System.currentTimeMillis()) // 获取系统当前时间
    private var time = dateFormat.format(date)

    fun insertNote(title: String, content: String): Long {
        val note = Note(title, content, time)
        return noteDao.insertNote(note)
    }

    fun deleteNote(id: Int) {
        thread {
            noteDao.deleteNote(id)
        }
    }

    fun updateNote(id: Int, title: String, content: String) {
        val newNote = Note(title, content, time)
        newNote.id = id.toLong()
        thread {
            noteDao.updateNote(newNote)
        }
    }


    fun queryAllNote(): LiveData<List<Note>> {
        return noteDao.queryAllNote()
    }

    fun queryAllNote2(): List<Note> {
        return noteDao.queryAllNote2()
    }


    fun queryOneNoteByIdAndContent(id: Int, content: String, title: String): List<Note> {
        return noteDao.queryOneNoteByIdAndContent(id, content, title)
    }

    fun queryNoteByKeyword(keyword: String): List<Note> {
        return noteDao.queryNoteByKeyword(keyword)
    }
}