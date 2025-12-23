package com.xiaosuli.notepad

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.xiaosuli.notepad.databinding.NoteEditBinding
import kotlin.concurrent.thread

class AddActivity : AppCompatActivity() {

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
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        // 修改状态栏字体颜色，用AndroidX官方兼容API
        val wic = ViewCompat.getWindowInsetsController(window.decorView)
        // true表示Light Mode，状态栏字体呈黑色，反之呈白色
        wic?.isAppearanceLightStatusBars = application.resources.configuration.uiMode == 0x11
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[NoteViewModel::class.java]
        showSoftInputFromWindow(binding.editContent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                insertNote()
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    insertNote()
                    finish()
                }
            })
        }
    }

    // 打开软键盘
    private fun showSoftInputFromWindow(editText: EditText) {
        editText.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_toolbar, menu)
        // menu?.findItem(R.id.delete)?.setVisible(false)  // 隐藏菜单栏其中的某一项
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                binding.editTitle.setText("")
                binding.editContent.setText("")
                finish()
            }

            R.id.clear -> {
                binding.editTitle.setText("")
                binding.editContent.setText("")
            }

            R.id.save -> {
                insertNote()
                finish()
            }

            android.R.id.home -> {
                insertNote()
                finish()
            }
        }
        return true
    }

    private fun insertNote() {
        val title: String = binding.editTitle.text.toString().trim { it <= ' ' }
        val content: String = binding.editContent.text.toString().trim { it <= ' ' }
        if ("" != content || "" != title) {
            thread {
                noteViewModel.insertNote(title, content)
            }
        }
    }
}