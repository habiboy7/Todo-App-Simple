package com.example.todolist.uai.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todolist.data.entitiy.TaskEntity

// Warna Kustom dari file Auth (untuk konsistensi)
private val PrimaryBlue = androidx.compose.ui.graphics.Color(0xFF1E3A8A)

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggle: (TaskEntity) -> Unit,
    onEditClick: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    showActions: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onToggle(task) }, // Toggle status saat Card diklik
        colors = CardDefaults.cardColors(
            containerColor = if (task.isDone) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (task.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                if (task.description?.isNotBlank() == true) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            if (showActions) {
                // Tampilkan ikon Edit/Delete hanya jika showActions = true
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onEditClick(task) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDelete(task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggle(task) },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            // ---------------------------------------------
        }
    }
}


@Composable
fun EditTaskDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String?) -> Unit,
    onDelete: (TaskEntity) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Tugas") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Tugas") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, if (description.isBlank()) null else description)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Delete di bagian kiri bawah dialog
                TextButton(
                    onClick = {
                        onDelete(task)
                        onDismiss()
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
                // Tombol Batal di bagian kanan bawah dialog
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        }
    )
}
