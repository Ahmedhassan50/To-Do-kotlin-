package com.example.todo.db.repositories

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todo.db.local.TaskDao
import com.example.todo.db.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository (private val taskDao: TaskDao){


    suspend fun addOrUpdateTasks(task: Task)= withContext(Dispatchers.IO){
        taskDao.addOrUpdateTasks(task)
    }



   suspend  fun deleteTask(task: Task)= withContext(Dispatchers.IO){
         taskDao.deleteTask(task)
     }


   suspend fun getTasks():List<Task> = withContext(Dispatchers.IO){
       taskDao.getTasks()
   }


}