package com.example.todo.db.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.db.model.Task


private const val DATABASE_NAME="tasks_database"

@Database(entities = [Task::class],version = 1,exportSchema = false)
abstract class TasksDataBase :RoomDatabase(){

    abstract fun taskDao():TaskDao


    companion object{
        @Volatile
        private var instance: TasksDataBase? = null

        fun getInstance(context:Context):TasksDataBase{
            return instance?: synchronized(Any()){
                instance?: buildDatabase(context).also{
                    instance= it
                }
            }
        }

        private fun buildDatabase(context: Context): TasksDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                TasksDataBase::class.java,
                DATABASE_NAME
            ).build()
        }

    }

}