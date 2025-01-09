package com.example.galerio.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.galerio.data.model.MediaItem
import com.example.galerio.data.model.MediaType
import com.example.galerio.ui.components.ImageCard
import com.example.galerio.ui.components.VideoCard
import com.example.galerio.utils.MediaUtils.getDeviceMedia
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// Función para mostrar la lista de imágenes en una cuadrícula
@Composable
fun MediaList(modifier: Modifier, context: Context, onMediaClick: (Uri, MediaType) -> Unit) {
    // Use remember to avoid recomputing the list unnecessarily
    var mediaItems by remember { mutableStateOf(emptyList<MediaItem>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val items = getDeviceMedia(context) // Should be run in a background thread
            mediaItems = items
            Log.d("MediaList", "Media items loaded: $mediaItems")
        }
    }

    // Agrupamos por fecha (utilizando la fecha tomada del media)
    val formatter = remember { DateTimeFormatter.ofPattern("EEE, d MMM", Locale.getDefault()) }
    val groupedMediaItems = mediaItems.groupBy { mediaItem ->
        val timestampInMillis = mediaItem.dateModified * 1000 // Convertir segundos a milisegundos

        // Convertir el timestamp a una fecha formateada
        Instant.ofEpochMilli(timestampInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    }

    if (groupedMediaItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "No media found",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
    else (
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize()
        ) {
            groupedMediaItems.forEach { (date, mediaForDate) ->
                // StickyHeader o cabecera
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                items(mediaForDate.sortedByDescending { it.dateModified }) { mediaItem ->
                    when (mediaItem.type) {
                        MediaType.Image -> ImageCard(imageUri = mediaItem.uri, context = context,
                            onClick = { onMediaClick(mediaItem.uri, mediaItem.type) })

                        MediaType.Video -> VideoCard(videoUri = mediaItem.uri,
                            duration = mediaItem.duration,
                            context = context,
                            onClick = { onMediaClick(mediaItem.uri, mediaItem.type) })
                    }
                }
            }
        }
    )
}