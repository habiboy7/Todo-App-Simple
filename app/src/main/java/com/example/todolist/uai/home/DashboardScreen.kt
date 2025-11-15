package com.example.todolist.uai.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.uai.common.TaskItem
import com.example.todolist.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

// Warna Kustom
private val PrimaryBlue = Color(0xFF1E3A8A)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DashboardScreen(
    vm: TaskViewModel,
    nav: NavController,
    onLogout: () -> Unit,
    onNavigateToList: () -> Unit
) {
    val tasks by vm.tasks.collectAsState()
    var newTaskTitle by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // State untuk Dialog Edit (DIHAPUS DARI DASHBOARD)
    // var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) } // <-- DIHAPUS

    fun addTaskAndClear() {
        if (newTaskTitle.isNotBlank()) {
            vm.addTask(newTaskTitle)
            newTaskTitle = "" // Bersihkan input
            keyboardController?.hide() // Sembunyikan keyboard
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Header Drawer
                Text(
                    "Menu Aplikasi",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
                // Item Navigasi
                NavigationDrawerItem(
                    label = { Text("Dashboard (Input Cepat)") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Sudah di Dashboard, tidak perlu navigasi
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Daftar Todo (Edit/Hapus)") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToList() // Pindah ke screen Daftar Todos
                    }
                )
                Spacer(Modifier.height(16.dp))
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard Todo") },
                    navigationIcon = {
                        // Icon Burger Menu
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
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
                // --- Input Cepat di Bawah Navbar ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text("Tulis tugas baru...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                addTaskAndClear() // Aksi saat Enter ditekan
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { addTaskAndClear() }, // Aksi saat tombol Add diklik
                        enabled = newTaskTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }

                Divider()

                // --- Daftar Tugas ---
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onToggle = { vm.toggleDone(it) },
                            onDelete = {},
                            onEditClick = {},
                            showActions = false
                        )
                    }
                }
            }
        }
    }


}