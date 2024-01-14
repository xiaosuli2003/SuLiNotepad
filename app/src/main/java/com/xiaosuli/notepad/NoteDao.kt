package com.xiaosuli.notepad

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: Note): Long

    @Query("insert into Note (title,content,time) values(:title,:content,:time)")
    fun insertNote(title: String, content: String, time: String): Long

    @Query("delete from Note where id = :id")
    fun deleteNote(id: Int)

    @Update
    fun updateNote(vararg newNote: Note)

    @Query("select * from Note")
    fun queryAllNote(): LiveData<List<Note>>

    @Query("select * from Note")
    fun queryAllNote2(): List<Note>

    @Query("select * from Note where id=:id and content=:content and title=:title")
    fun queryOneNoteByIdAndContent(id: Int, content: String, title: String): List<Note>

    @Query("select * from Note where title like '%' || :keyword || '%' or content like '%' || :keyword || '%'")
    fun queryNoteByKeyword(keyword: String): List<Note>
}