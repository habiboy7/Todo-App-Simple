package com.example.todolist.uai.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.data.entitiy.TaskEntity
import com.example.todolist.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(vm: TaskViewModel, onLogout: () -> Unit) {
    val tasks by vm.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Todo") }, actions = {
                IconButton(onClick = onLogout) { Text("Logout") }
            })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                onClick = {
                    vm.addTask("Task from FAB ${System.currentTimeMillis()}", null)
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(tasks) { task ->
                TaskItem(task, onToggle = { vm.toggleDone(it) }, onDelete = { vm.deleteTask(it) })
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskEntity, onToggle: (TaskEntity) -> Unit, onDelete: (TaskEntity) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                task.description?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            Column {
                Text(if (task.isDone) "Done" else "Open")
                Spacer(Modifier.height(8.dp))
                Row {
                    Text("Toggle", modifier = Modifier.clickable { onToggle(task) }.padding(8.dp))
                    Text("Delete", modifier = Modifier.clickable { onDelete(task) }.padding(8.dp))
                }
            }
        }
    }
}
