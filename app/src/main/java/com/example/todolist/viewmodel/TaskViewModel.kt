package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.database.TaskDatabase
import com.example.todolist.data.entitiy.TaskEntity
import com.example.todolist.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: TaskRepository

    val tasks = TaskDatabase.getInstance(application).taskDao().getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        val dao = TaskDatabase.getInstance(application).taskDao()
        repo = TaskRepository(dao)
    }

    fun addTask(title: String, description: String? = null) = viewModelScope.launch {
        repo.insert(TaskEntity(title = title, description = description))
    }

    fun toggleDone(task: TaskEntity) = viewModelScope.launch {
        repo.update(task.copy(isDone = !task.isDone))
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repo.delete(task)
    }
}
