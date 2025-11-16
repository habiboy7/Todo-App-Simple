# 📱 Aplikasi TodoList

Aplikasi Android Todolist sederhana yang dibangun menggunakan kotlin dan jetpack compose

## ✨ Fitur Utama

Aplikasi ini menampilkan fitur-fitur inti pengembangan Android modern:

- 🔐 **Autentikasi Pengguna**  
  Login & Register sederhana menggunakan `SharedPreferences`.

- 🗂️ **Database Lokal (CRUD)**  
  Tambah, baca, edit, dan hapus tugas menggunakan Room.

- 💾 **Persistensi Data**  
  Semua data disimpan secara lokal di perangkat.

- 🧭 **Navigasi Modern**  
  Navigation Compose + Drawer Menu untuk berpindah antar layar.

- 📱 **Dua Layar Fungsional**  
  - **Dashboard** → Input cepat tugas  
  - **TodosScreen** → Edit & hapus tugas

- 🧠 **Manajemen Status**  
  Menggunakan ViewModel untuk memisahkan logika bisnis dari UI.


## 🛠️ Arsitektur & Teknologi yang Digunakan

| Komponen | Teknologi |
|--------|-----------|
| Bahasa | **Kotlin** |
| UI | **Jetpack Compose** |
| Arsitektur | **MVVM** |
| Database | **Room** |
| Asinkron | **Coroutines & Flow** |
| Navigasi | **Navigation Compose** |
| Dependency Injection | Custom `ViewModelProvider.Factory` |
| State Management | `ViewModel` + `StateFlow` |



