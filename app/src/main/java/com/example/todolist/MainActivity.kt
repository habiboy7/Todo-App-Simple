package com.example.todolist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.uai.auth.LoginScreen
import com.example.todolist.uai.auth.RegisterScreen
import com.example.todolist.uai.home.TodoScreen
import com.example.todolist.viewmodel.TaskViewModel
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    private val vm: TaskViewModel by viewModels() // ViewModel provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppEntry(vm)
        }
    }
}

@Composable
fun AppEntry(vm: TaskViewModel) {
    val nav = rememberNavController()
    val ctx = LocalContext.current

    NavHost(navController = nav, startDestination = "login") {
        composable("login") {
            LoginScreen(nav, onLoginSuccess = {
                nav.navigate("todo") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("register") {
            RegisterScreen(nav, onRegistered = {
                nav.popBackStack()
            })
        }

        composable("todo") {
            TodoScreen(vm = vm, onLogout = {
                ctx.getSharedPreferences("myapp_prefs", Context.MODE_PRIVATE)
                    .edit { putBoolean("is_logged_in", false) }

                nav.navigate("login") {
                    popUpTo("todo") { inclusive = true }
                }
            })
        }
    }
}
