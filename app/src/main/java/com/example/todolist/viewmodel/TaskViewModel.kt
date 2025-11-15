package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entitiy.TaskEntity
import com.example.todolist.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Menambah tugas baru ke database.
     * @param title Judul tugas.
     * @param description Deskripsi tugas (opsional).
     */
    fun addTask(title: String, description: String? = null) {
        if (title.isBlank()) return // Jangan masukkan tugas kosong
        val newTask = TaskEntity(title = title, description = description)
        viewModelScope.launch {
            repository.insert(newTask)
        }
    }

    /**
     * Mengubah status isDone (selesai/belum) dari tugas.
     */
    fun toggleDone(task: TaskEntity) {
        viewModelScope.launch {
            val updatedTask = task.copy(isDone = !task.isDone)
            repository.update(updatedTask)
        }
    }

    /**
     * Mengupdate judul dan deskripsi tugas yang sudah ada.
     */
    fun updateTask(task: TaskEntity, newTitle: String, newDescription: String?) {
        if (newTitle.isBlank()) return // Jangan update dengan judul kosong
        viewModelScope.launch {
            val updatedTask = task.copy(title = newTitle, description = newDescription)
            repository.update(updatedTask)
        }
    }

    /**
     * Menghapus tugas dari database.
     */
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}