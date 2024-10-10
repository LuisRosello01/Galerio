package com.example.galerio.ui

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.galerio.data.model.MediaType
import com.example.galerio.permissions.RequestMediaPermissions

// Función para mostrar la pantalla principal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context, onVideoClick: (Uri) -> Unit) {
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) } // Estado para la selección de medios
    var isVideoSelected by remember { mutableStateOf(false) } // Estado para saber si se seleccionó un video
    var isTopBarVisible by remember { mutableStateOf(true) } // Estado para controlar la visibilidad de la barra de aplicación

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior() // Manejo de desplazamiento para la barra de aplicación

    Scaffold(
        topBar = {
            if (isTopBarVisible) MyAppBar(scrollBehavior)
                 },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        RequestMediaPermissions {
            if (selectedMediaUri == null) {
                isTopBarVisible =
                    true // Mostrar la barra de aplicación cuando no hay un medio seleccionado
            }
                // Lista de medios (fondo)
                MediaList(
                    modifier = Modifier.padding(paddingValues),
                    context = context
                ) { uri, isVideo ->
                    selectedMediaUri = uri
                    isVideoSelected = isVideo == MediaType.Video
                    isTopBarVisible =
                        false // Ocultar la barra de aplicación cuando se selecciona un medio
                }
                // Si hay una imagen o video seleccionado, lo mostramos superpuesto
                selectedMediaUri?.let { uri ->
                    if (isVideoSelected) {
                        VideoPlayerScreen(videoUri = uri) {
                            selectedMediaUri = null // Cerrar el video y volver a la lista
                            isTopBarVisible = true // Mostrar la barra de aplicación
                        }
                    } else {
                        FullScreenImage(imageUri = uri) {
                            selectedMediaUri = null // Cerrar la imagen y volver a la lista
                            isTopBarVisible = true // Mostrar la barra de aplicación
                        }
                }
            }
        }
    }
}

// Función para mostrar la barra de aplicación
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text("Galerio") },
        actions = {
            IconButton(onClick = {
                /*TODO*/
            }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More")
            }
        },
        scrollBehavior = scrollBehavior
    )
}