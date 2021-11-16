package com.example.todo.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.db.local.TasksDataBase
import com.example.todo.db.model.Task
import com.example.todo.db.model.TaskStatus
import com.example.todo.db.repositories.TaskRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class TaskViewModel(application:Application):AndroidViewModel(application) {

    private var taskRepository:TaskRepository
    var newTasksLiveData:MutableLiveData<List<Task>> =MutableLiveData()
    var doneTasksLiveData:MutableLiveData<List<Task>> =MutableLiveData()
    var archiveTasksLiveData:MutableLiveData<List<Task>> =MutableLiveData()


    init {
        val dateBaseInstance=TasksDataBase.getInstance(application)
        taskRepository= TaskRepository(dateBaseInstance.taskDao())

    }


    fun addTask(task: Task) =viewModelScope.launch{
        try {
            taskRepository.addOrUpdateTasks(task)
            getAllTasks()
        } catch (e: Exception) {
            Log.e("llllllist",e.message.toString())
        }
    }

    fun getAllTasks() =viewModelScope.launch{
        try {
            val result:List<Task> = taskRepository.getTasks()
            val newTasks:MutableList<Task> = mutableListOf()
            val doneTasks:MutableList<Task> = mutableListOf()
            val archiveTasks:MutableList<Task> = mutableListOf()

            result.forEach {
                when(it.status){
                    TaskStatus.NEW->newTasks.add(it)
                    TaskStatus.DONE->doneTasks.add(it)
                    TaskStatus.ARCHIVE->archiveTasks.add(it)
                }
            }

            newTasksLiveData.postValue(newTasks)
            doneTasksLiveData.postValue(doneTasks)
            archiveTasksLiveData.postValue(archiveTasks)

        } catch (e: Exception) {
            Log.e("llllllist",e.message.toString())
        }

    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        try {
            taskRepository.deleteTask(task)
            getAllTasks()
        } catch (e: Exception) {
            Log.e("llllllist",e.message.toString())
        }
    }



}