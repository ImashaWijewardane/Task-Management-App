package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.models.ToDoEntity
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker

class AddTodoActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    private lateinit var imgCheck: ImageView
    private lateinit var imgDelete: ImageView
    private lateinit var imgBackArrow: ImageView
    private lateinit var calendarBtn: ImageButton


    private lateinit var todo: ToDoEntity
    private lateinit var oldTodo: ToDoEntity
    private var isUpdate = false
    private var deadline = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        etTitle = findViewById(R.id.et_title)
        etNote = findViewById(R.id.et_note)
        imgCheck = findViewById(R.id.img_check)
        imgDelete = findViewById(R.id.img_delete)
        imgBackArrow = findViewById(R.id.img_back_arrow)
        calendarBtn = findViewById(R.id.calendarBtn)


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
                    ToDoEntity(oldTodo.id, title, todoDescription, formatter.format(Date()),deadline,false)
                } else {
                    ToDoEntity(null, title, todoDescription, formatter.format(Date()),deadline,false)
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

        calendarBtn.setOnClickListener {
            showDateTimePickerDialog(this)
        }
    }

    private fun showDateTimePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Date Picker Dialog
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(year, monthOfYear, dayOfMonth)
                    }.time
                )
                showTimePickerDialog(context, selectedDate)
            },
            year, month, day
        )

        // Show the Date Picker Dialog
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(context: Context, selectedDate: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }.time
                )
                deadline = "$selectedDate $selectedTime"

                Toast.makeText(context, "Selected Date and Time: $deadline", Toast.LENGTH_LONG).show()
                // Assuming dateTxt is your TextView where you want to display the date and time
                // dateTxt.text = selectedDateTime
            },
            hour, minute, false
        )

        // Show the Time Picker Dialog
        timePickerDialog.show()
    }


}
