package com.example.galerio.ui.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun VideoCard(videoUri: Uri, duration: Long?, onClick: () -> Unit) {

    val context = LocalContext.current

    // Genera la miniatura del video dependiendo de la versión de Android
    val videoThumbnail = remember(videoUri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // API 29 o superior: usa content resolver para obtener miniatura
                context.contentResolver.loadThumbnail(videoUri, Size(200, 200), null)
            } else {
                // API inferior a 29: usa el método antiguo con content resolver
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
                    bitmap = videoThumbnail.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Si no se puede generar la miniatura, muestra un color de fondo
                Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
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

fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d ▶️", minutes, seconds)
}