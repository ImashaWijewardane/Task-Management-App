package com.example.taskmanager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.adaptors.TodoAdapter
import com.example.taskmanager.models.ToDoEntity
import com.example.taskmanager.models.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TodoAdapter.TodoClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddTodo: FloatingActionButton
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter
    private lateinit var searchTxt: EditText
    private lateinit var searchBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(TodoViewModel::class.java)

        viewModel.allTodo.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }
    }

    private fun initUI() {
        recyclerView = findViewById(R.id.recycler_view)
        fabAddTodo = findViewById(R.id.fab_add_todo)
        searchTxt = findViewById(R.id.et_search)
        searchBtn = findViewById(R.id.searchBtn)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TodoAdapter(this)
        recyclerView.adapter = adapter

        fabAddTodo.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivityForResult(intent, ADD_TODO_REQUEST_CODE)
        }
        searchBtn.setOnClickListener {
            val searchText = searchTxt.text.toString().trim()
            if (searchText.isNotEmpty()) {
                viewModel.searchTodos(searchText).observe(this) { todos ->
                    todos?.let {
                        adapter.updateList(todos)
                    }
                }
            } else {
//                Toast.makeText(this, "Please enter search text", Toast.LENGTH_SHORT).show()
                viewModel.allTodo.observe(this) { list ->
                    list?.let {
                        adapter.updateList(list)
                    }
                }
            }
        }
//        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//            0,
//            ItemTouchHelper.RIGHT // Allow swipe right only
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                val todo = adapter.getItemAtPosition(position)
//                todo?.let {
//                    // Set isCompleted field to true
//                    it.isCompleted = true
//                    viewModel.updateTodo(it)
//                }
//            }
//        })
//        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            val todo = data?.getSerializableExtra("todo") as? ToDoEntity
            if (todo != null) {
                viewModel.insertTodo(todo)
            }
        } else if (requestCode == UPDATE_DELETE_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            val todo = data?.getSerializableExtra("todo") as? ToDoEntity
            val isDeleted = data?.getSerializableExtra("delete_todo") as? Boolean
            if (todo != null && isDeleted == null) {
                viewModel.updateTodo(todo)
            } else {
                todo?.let {
                    viewModel.deleteTodo(it)
                }
            }
        }
    }

    override fun onItemClicked(todo: ToDoEntity) {
        val intent = Intent(this@MainActivity, AddTodoActivity::class.java)
        intent.putExtra("current_todo", todo)
        startActivityForResult(intent, UPDATE_DELETE_TODO_REQUEST_CODE)
    }

    companion object {
        private const val ADD_TODO_REQUEST_CODE = 1
        private const val UPDATE_DELETE_TODO_REQUEST_CODE = 2
        private const val UPDATE_TODO_REQUEST_CODE = 3
    }
}
