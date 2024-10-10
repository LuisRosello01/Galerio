package com.example.galerio.ui

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun FullScreenImage(imageUri: Uri?, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(0.5f) } // Estado inicial de escala para el zoom-in/out
    var offset by remember { mutableStateOf(Offset.Zero) } // Para el desplazamiento
    val animatedScale by animateFloatAsState(targetValue = if (scale > 0.5f) scale else 1f) // Animación de escala

    val scope = rememberCoroutineScope() // Para controlar la animación

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Cierra la imagen si se ha arrastrado lo suficiente
                        if (offset.x > 300 || offset.x < -300 || offset.y > 300 || offset.y < -300) {
                            scope.launch {
                                onDismiss() // Cierra al arrastrar más de 300 píxeles
                            }
                        } else {
                            offset = Offset.Zero // Restablecer el desplazamiento
                        }
                    },
                    onDrag = { _, dragAmount ->
                        offset += dragAmount
                    }
                )
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = animatedScale,
                    scaleY = animatedScale,
                    translationX = offset.x,
                    translationY = offset.y
                ),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = null,
                //contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // Lanzar la animación de zoom-in al entrar en la pantalla
    LaunchedEffect(Unit) {
        scale = 1f
    }
}