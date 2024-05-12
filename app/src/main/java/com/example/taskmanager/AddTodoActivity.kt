package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.models.ToDoEntity
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    private lateinit var imgCheck: ImageView
    private lateinit var imgDelete: ImageView
    private lateinit var imgBackArrow: ImageView

    private lateinit var todo: ToDoEntity
    private lateinit var oldTodo: ToDoEntity
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        etTitle = findViewById(R.id.et_title)
        etNote = findViewById(R.id.et_note)
        imgCheck = findViewById(R.id.img_check)
        imgDelete = findViewById(R.id.img_delete)
        imgBackArrow = findViewById(R.id.img_back_arrow)

        try {
            oldTodo = intent.getSerializableExtra("current_todo") as ToDoEntity
            etTitle.setText(oldTodo.title)
            etNote.setText(oldTodo.note)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isUpdate) {
            imgDelete.visibility = View.VISIBLE
        } else {
            imgDelete.visibility = View.INVISIBLE
        }

        imgCheck.setOnClickListener {
            val title = etTitle.text.toString()
            val todoDescription = etNote.text.toString()

            if (title.isNotEmpty() && todoDescription.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                todo = if (isUpdate) {
                    ToDoEntity(oldTodo.id, title, todoDescription, formatter.format(Date()))
                } else {
                    ToDoEntity(null, title, todoDescription, formatter.format(Date()))
                }
                val intent = Intent()
                intent.putExtra("todo", todo)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddTodoActivity, "Please enter some data", Toast.LENGTH_LONG).show()
            }
        }

        imgDelete.setOnClickListener {
            val intent = Intent()
            intent.putExtra("todo", oldTodo)
            intent.putExtra("delete_todo", true)
            setResult(RESULT_OK, intent)
            finish()
        }

        imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}
