package com.example.todo.db.local

import androidx.room.*
import com.example.todo.db.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateTasks(task: Task)


    @Delete
    suspend fun deleteTask(task: Task)

    @Query("Select * from new_tasks")
    suspend fun getTasks():List<Task>

}