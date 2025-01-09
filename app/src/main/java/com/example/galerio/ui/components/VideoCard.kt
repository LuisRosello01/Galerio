package com.example.galerio.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun VideoCard(videoUri: Uri, duration: Long?, context: Context, onClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    // Genera la miniatura del video dependiendo de la versión de Android
    var videoThumbnail by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoUri) {
        coroutineScope.launch {
            videoThumbnail = loadVideoThumbnail(videoUri, context)
        }
    }

    Card(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            if (videoThumbnail != null) {
                // Muestra la miniatura generada del video
                Image(
                    bitmap = videoThumbnail!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Si no se puede generar la miniatura, muestra un color de fondo
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray))
            }

            // Duración del video
            Text(
                text = formatDuration(duration ?: 0),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

suspend fun loadVideoThumbnail(videoUri: Uri, context: Context): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(videoUri, Size(200, 200), null)
            } else {
                val fileDescriptor = context.contentResolver.openFileDescriptor(videoUri, "r")?.fileDescriptor
                if (fileDescriptor != null) {
                    ThumbnailUtils.createVideoThumbnail(
                        fileDescriptor.toString(),
                        MediaStore.Video.Thumbnails.MINI_KIND
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d ▶️", minutes, seconds)
}