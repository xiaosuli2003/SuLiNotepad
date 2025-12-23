package com.xiaosuli.notepad

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xiaosuli.notepad.databinding.ActivityMainBinding
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var mainViewModel: NoteViewModel

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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
        binding.addNote.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
        mainViewModel = ViewModelProvider(
            owner = this
        )[NoteViewModel::class.java]
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        val noteLists = ArrayList<Note>()
        adapter = NoteAdapter(this, noteLists)
        binding.recyclerView.adapter = adapter
        mainViewModel.queryAllNote().observe(this) {
            adapter.noteList = it
            adapter.notifyDataSetChanged()

        }
        binding.search.setOnClickListener {
            searchNote()
        }
        binding.swipeRefresh.apply {
            setColorSchemeResources(R.color.yellow)
            setOnRefreshListener {
                closeIMMAndClearFocus()
                refreshNote(150)
            }
        }
        binding.clear.apply {
            isVisible = false
            setOnClickListener {
                closeIMMAndClearFocus()
            }
        }
        binding.searchEdit.apply {
            setOnEditorActionListener { _, actionId, _ ->
                // 如果actionId是搜索的id，则进行下一步的操作
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchNote()
                }
                true
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(editable: Editable?) {
                    binding.clear.isVisible = !"".contentEquals(editable)
                }
            })
        }
    }

    private fun searchNote() {
        val keyword = binding.searchEdit.text.toString().trim()
        if (keyword != "") {
            thread {
                val notes = mainViewModel.queryNoteByKeyword(keyword)
                runOnUiThread {
                    if (notes.isEmpty()) {
                        Toast.makeText(this, "未查询到便签", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.noteList = notes
                        adapter.notifyDataSetChanged()
                    }
                }

            }
        } else {
            closeIMMAndClearFocus()
        }
    }

    private fun refreshNote(min: Long) {
        thread {
            Thread.sleep(min)
            val notes = mainViewModel.queryAllNote2()
            runOnUiThread {
                adapter.noteList = notes
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        closeIMMAndClearFocus()
        refreshNote(0)
    }

    private fun closeIMMAndClearFocus() {
        // 关闭软键盘,并让输入框失去焦点
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            binding.searchEdit.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        binding.searchEdit.apply {
            setText("")
            clearFocus()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_list_mode -> {
                if (mainViewModel.flag) {
                    val layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.layoutManager = layoutManager
                    mainViewModel.flag = false
                } else {
                    val layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.recyclerView.layoutManager = layoutManager
                    mainViewModel.flag = true
                }
            }
        }
        return true
    }
}