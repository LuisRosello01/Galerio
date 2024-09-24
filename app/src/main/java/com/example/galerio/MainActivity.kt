package com.example.galerio

import PhotoAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView
import com.example.galerio.data.model.Photo
import com.example.galerio.ui.theme.GalerioTheme
import com.example.galerio.viewmodel.PhotosViewModel

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private var photoList = mutableListOf<Photo>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // El permiso fue otorgado, puedes acceder a la galería
            } else {
                // Manejar el caso en que el permiso sea denegado
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        setContent {
            MyScreen()
        }
    }
}

@Composable
fun MyScreen() {
    Scaffold(
        topBar = { MyAppBar() }
    ) {
        GridMediaList()
    }
}

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

@Composable
fun GridMediaList() {

}

@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
    MyScreen()
}