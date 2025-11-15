package com.example.todolist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.data.database.TaskDatabase
import com.example.todolist.repository.TaskRepository
import com.example.todolist.uai.auth.LoginScreen
import com.example.todolist.uai.auth.RegisterScreen
import com.example.todolist.uai.home.DashboardScreen
import com.example.todolist.uai.home.TodosScreen
import com.example.todolist.viewmodel.TaskViewModel
import androidx.core.content.edit


class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppEntry()
        }
    }
}

@Composable
fun AppEntry() {
    val ctx = LocalContext.current
    val nav = rememberNavController()

    val db = remember { TaskDatabase.getInstance(ctx) }
    val repository = remember { TaskRepository(db.taskDao()) }
    val factory = remember { TaskViewModelFactory(repository) }
    val vm: TaskViewModel = viewModel(factory = factory)

    val prefs = ctx.getSharedPreferences("myapp_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = remember { prefs.getBoolean("is_logged_in", false) }
    val startDestination = if (isLoggedIn) "dashboard" else "login"

    NavHost(navController = nav, startDestination = startDestination) {
        composable("login") {
            LoginScreen(nav, onLoginSuccess = {
                nav.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("register") {
            RegisterScreen(nav, onRegistered = {
                // Setelah register, kembali ke login
                nav.popBackStack()
            })
        }

        composable("dashboard") {
            DashboardScreen(
                vm = vm,
                nav = nav,
                onLogout = {
                    prefs.edit { putBoolean("is_logged_in", false) }
                    nav.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToList = { nav.navigate("todos_list") }
            )
        }

        composable("todos_list") {
            TodosScreen(vm = vm, nav = nav)
        }
    }
}