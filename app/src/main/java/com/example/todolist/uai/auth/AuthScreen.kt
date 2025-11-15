package com.example.todolist.uai.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController



val PrimaryBlue = Color(0xFF1E3A8A) // judul
val LightGrayBackground = Color(0xFFF0F4F8) // Warna background TextField

@Composable
fun LoginScreen(nav: NavController, onLoginSuccess: () -> Unit) {
    val ctx = LocalContext.current
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "Login",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Deskripsi
        Text(
            text = "Welcome back brooww!",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Email Field
        CustomAuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )
        Spacer(Modifier.height(16.dp))

        // Password Field
        CustomAuthTextField(
            value = pass,
            onValueChange = { pass = it },
            label = "Password",
            isPassword = true
        )
        Spacer(Modifier.height(32.dp))

        // Sign In Button
        Button(
            onClick = {
                if (email.isBlank() && pass.isBlank()) {
                    Toast.makeText(ctx, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
                    return@Button // Keluar dari lambda onClick
                }
                if (email.isBlank()) {
                    Toast.makeText(ctx, "Email harus diisi", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (pass.isBlank()) {
                    Toast.makeText(ctx, "Password harus diisi", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val prefs = ctx.getSharedPreferences("myapp_prefs", Context.MODE_PRIVATE)
                val savedEmail = prefs.getString("email", null)
                val savedPass = prefs.getString("password", null)
                if (email == savedEmail && pass == savedPass) {
                    prefs.edit { putBoolean("is_logged_in", true) }
                    onLoginSuccess()
                }  else {
                    Toast.makeText(ctx, "Email atau password salah", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Sign in", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(24.dp))
        // Teks untuk navigasi ke Register
        TextButton(onClick = { nav.navigate("register") }) {
            Text(
                "Don't have an account? Sign up",
                color = PrimaryBlue,
                fontSize = 14.sp
            )
        }
    }
}





@Composable
fun RegisterScreen(nav: NavController, onRegistered: () -> Unit) {
    val ctx = LocalContext.current
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Deskripsi
        Text(
            text = "Kepengen produktif, yuk buat akun baru! sing santun",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Email Field
            CustomAuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )
        Spacer(Modifier.height(16.dp))

        // Password Field
        CustomAuthTextField(
            value = pass,
            onValueChange = { pass = it },
            label = "Password",
            isPassword = true
        )
        Spacer(Modifier.height(16.dp))

        // Confirm Password Field
        CustomAuthTextField(
            value = confirmPass,
            onValueChange = { confirmPass = it },
            label = "Confirm Password",
            isPassword = true
        )
        Spacer(Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {

                if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
                    Toast.makeText(ctx, "Semua kolom harus diisi untuk pendaftaran", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (pass != confirmPass) {
                    Toast.makeText(ctx, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Jika semua validasi lolos: Simpan data dan navigasi
                val prefs = ctx.getSharedPreferences("myapp_prefs", Context.MODE_PRIVATE)
                prefs.edit { putString("email", email).putString("password", pass) }
                onRegistered()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Sign up", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(24.dp))
        // Teks untuk navigasi ke Login
        TextButton(onClick = { nav.navigate("login") }) {
            Text(
                "Sign in",
                color = PrimaryBlue,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CustomAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = LightGrayBackground,
            unfocusedContainerColor = LightGrayBackground,
            disabledContainerColor = LightGrayBackground,
            focusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah
            unfocusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah
            cursorColor = PrimaryBlue,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        ),
        singleLine = true,
        visualTransformation = if (isPassword)
            PasswordVisualTransformation() else VisualTransformation.None
    )
}