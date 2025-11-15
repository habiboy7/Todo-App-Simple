package com.example.todolist.repository

import com.example.todolist.data.dao.TaskDao
import com.example.todolist.data.entitiy.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<TaskEntity>> = dao.getAllTasks()
    suspend fun insert(task: TaskEntity) = dao.insert(task)
    suspend fun update(task: TaskEntity) = dao.update(task)
    suspend fun delete(task: TaskEntity) = dao.delete(task)
}
