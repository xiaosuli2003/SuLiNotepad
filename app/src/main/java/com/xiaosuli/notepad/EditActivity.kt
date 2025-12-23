package com.xiaosuli.notepad

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.xiaosuli.notepad.databinding.NoteEditBinding
import kotlin.concurrent.thread

class EditActivity : AppCompatActivity() {

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_TITLE = "title"
        const val FIELD_CONTENT = "content"

        fun actionStart(context: Context, id: Int, title: String, content: String) {
            val intent = Intent(context, EditActivity::class.java).apply {
                putExtra(FIELD_ID, id)
                putExtra(FIELD_TITLE, title)
                putExtra(FIELD_CONTENT, content)
            }
            context.startActivity(intent)
        }
    }

    private var noteId = 0
    private var oldTitle = ""
    private var oldContent = ""
    private lateinit var noteViewModel: NoteViewModel

    private val binding by lazy {
        NoteEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 修改状态栏字体颜色，用AndroidX官方兼容API
        val wic = ViewCompat.getWindowInsetsController(window.decorView)
        // true表示Light Mode，状态栏字体呈黑色，反之呈白色
        wic?.isAppearanceLightStatusBars = application.resources.configuration.uiMode == 0x11
        noteId = intent.getIntExtra(FIELD_ID, 0)
        oldTitle = intent.getStringExtra(FIELD_TITLE) ?: ""
        oldContent = intent.getStringExtra(FIELD_CONTENT) ?: ""
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.editTitle.setText(oldTitle)
        binding.editContent.setText(oldContent)
        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[NoteViewModel::class.java]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                queryAndUpdateNote()
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    queryAndUpdateNote()
                    finish()
                }
            })
        }
    }

    private fun queryAndUpdateNote() {
        val newContent: String = binding.editContent.text.toString().trim { it <= ' ' }
        val newTitle: String = binding.editTitle.text.toString().trim { it <= ' ' }
        thread {
            val noteLists = noteViewModel.queryOneNoteByIdAndContent(noteId, newContent, newTitle)
            runOnUiThread {
                if (noteLists.isEmpty()) {
                    updateNote()
                }
            }
        }
    }

    private fun updateNote() {
        if (noteId != 0) {
            val newContent: String = binding.editContent.text.toString().trim { it <= ' ' }
            val newTitle: String = binding.editTitle.text.toString().trim { it <= ' ' }
            if ("" == newContent && "" == newTitle) {
                noteViewModel.deleteNote(noteId)
                finish()
                return
            }
            if (newContent != oldContent || oldTitle != newTitle) {
                noteViewModel.updateNote(noteId, newTitle, newContent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                noteViewModel.deleteNote(noteId)
                binding.editTitle.setText("")
                binding.editContent.setText("")
                finish()
            }

            R.id.clear -> {
                binding.editContent.setText("")
            }

            R.id.save -> {
                queryAndUpdateNote()
                // 关闭软键盘,并让两个输入框失去焦点
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    binding.editContent.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                binding.editContent.clearFocus()
                binding.editTitle.clearFocus()
            }

            android.R.id.home -> {
                queryAndUpdateNote()
                finish()
            }
        }
        return true
    }
}