package com.xiaosuli.notepad

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var _context: Context? = null

        val context: Context get() = _context!!
    }

    override fun onCreate() {
        super.onCreate()
        _context = applicationContext
    }
}