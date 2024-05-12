package com.example.taskmanager.Database

import androidx.lifecycle.LiveData
import com.example.taskmanager.models.ToDoEntity

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: LiveData<List<ToDoEntity>> = todoDao.getAllTodos()

    suspend fun insert(todo: ToDoEntity){
        todoDao.insert(todo)
    }

    suspend fun delete(todo: ToDoEntity){
        todoDao.delete(todo)
    }

    suspend fun update(todo: ToDoEntity){
        todoDao.update(todo.id, todo.title, todo.note)
    }
    fun searchTodos(searchText: String): LiveData<List<ToDoEntity>> {
        return todoDao.searchTodos("%$searchText%")
    }
}