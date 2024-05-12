package com.example.taskmanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "deadline") val deadline:String?,
    @ColumnInfo(name = "isCompleted") val isCompleted:Boolean
): java.io.Serializable