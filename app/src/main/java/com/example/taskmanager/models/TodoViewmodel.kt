package com.example.taskmanager.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.Database.TodoDatabase
import com.example.taskmanager.Database.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TodoRepository
    val allTodo : LiveData<List<ToDoEntity>>

    init {
        val dao = TodoDatabase.getDatabase(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodo = repository.allTodos
    }

    fun insertTodo(todo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(todo)
    }

    fun updateTodo(todo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.update(todo)
    }

    fun deleteTodo(todo: ToDoEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(todo)
    }
}