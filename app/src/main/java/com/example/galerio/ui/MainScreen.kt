package com.example.galerio.ui

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.galerio.permissions.RequestMediaPermissions

// Función para mostrar la pantalla principal
@Composable
fun MyScreen(context: Context, onVideoClick: (Uri) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = { MyAppBar() }
    ) { paddingValues ->
        RequestMediaPermissions {
            if (selectedImageUri == null) {
                // Aquí pasamos el contexto a MediaList
                MediaList(modifier = Modifier.padding(paddingValues), context = context) { uri ->
                    selectedImageUri = uri
                }
            } else {
                FullScreenMedia(mediaUri = selectedImageUri) {
                    selectedImageUri = null // Volver a la galería al cerrar la imagen completa
                }
            }
        }
    }
}

// Función para mostrar la barra de aplicación
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar() {
    TopAppBar(
        title = { Text("Galerio") },
        actions = {
            IconButton(onClick = {
                /*TODO*/
            }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More")
            }
        }
    )
}