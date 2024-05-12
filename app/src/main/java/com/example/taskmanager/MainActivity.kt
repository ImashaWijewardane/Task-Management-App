package com.example.taskmanager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TodoAdapter(this)
        recyclerView.adapter = adapter

        fabAddTodo.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivityForResult(intent, ADD_TODO_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            val todo = data?.getSerializableExtra("todo") as? ToDoEntity
            if (todo != null) {
                viewModel.insertTodo(todo)
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
    }
}
