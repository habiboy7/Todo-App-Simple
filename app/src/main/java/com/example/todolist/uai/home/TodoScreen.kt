package com.example.todolist.uai.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.data.entitiy.TaskEntity
import com.example.todolist.uai.common.EditTaskDialog
import com.example.todolist.uai.common.TaskItem
import com.example.todolist.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen(vm: TaskViewModel, nav: NavController) {
    val tasks by vm.tasks.collectAsState()
    // State untuk Dialog Edit
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Tugas (Edit & Hapus)") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali ke Dashboard")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                item {
                    Text(
                        text = "Klik item untuk mengubah status Selesai/Belum Selesai.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { vm.toggleDone(it) },
                        onEditClick = { taskToEdit = it },
                        onDelete = { vm.deleteTask(it) },
                    )
                }
            }
        }
    }

    // Dialog Edit (muncul di atas semua konten)
    taskToEdit?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { taskToEdit = null },
            onSave = { newTitle, newDesc -> vm.updateTask(task, newTitle, newDesc) },
            onDelete = { vm.deleteTask(it) }
        )
    }
}