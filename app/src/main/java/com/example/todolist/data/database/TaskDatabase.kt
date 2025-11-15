package com.example.todolist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.data.dao.TaskDao
import com.example.todolist.data.entitiy.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase =
            INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                                context.applicationContext,
                                TaskDatabase::class.java,
                                "task_db"
                            ).fallbackToDestructiveMigration(false).build()
                INSTANCE = inst
                inst
            }
    }
}
