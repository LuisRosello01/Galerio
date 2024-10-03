package com.example.galerio

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.galerio.ui.MyScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        setContent {
            MyScreen(context = this)
        }
    }
}

// Función de vista previa para mostrar la pantalla
@Preview(showBackground = true)
@Composable
fun PreviewMyScreen() {
        ActivityPreview {
            MyScreen(context = LocalContext.current)
        }
}

@Composable
fun ActivityPreview(content: @Composable () -> Unit) {
    val activity = LocalContext.current as? Activity
    if (activity != null) {
        AndroidView(
            factory = { context ->
                ComposeView(context).apply {
                    setContent {
                        content()
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = {
                it.setContent {
                    content()
                }
            }
        )
    }
}