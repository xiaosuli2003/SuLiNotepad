package com.xiaosuli.notepad

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Note::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        private var instance: AppDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): AppDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, "app_database"
            )
                .build().apply {
                    instance = this
                }
        }
    }
}