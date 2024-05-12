package com.example.taskmanager.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.models.ToDoEntity

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: ToDoEntity)

    @Delete
    suspend fun delete(todo: ToDoEntity)

    @Query("SELECT * from todo_table order by id ASC")
    fun getAllTodos(): LiveData<List<ToDoEntity>>

    @Query("UPDATE todo_table set title = :title, note = :note where id = :id")
    suspend fun update(id: Int?, title: String?, note: String?)
}