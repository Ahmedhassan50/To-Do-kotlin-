package com.example.todo.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="new_tasks")
data class Task(
 @PrimaryKey(autoGenerate = true)
 val id:Int,
 val title:String,
 val date:String,
 val time:String,
 var status:TaskStatus
)

enum class TaskStatus{
 NEW,DONE,ARCHIVE
}