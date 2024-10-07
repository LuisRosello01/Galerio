package com.example.galerio.ui

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.galerio.data.model.MediaType
import com.example.galerio.ui.components.ImageCard
import com.example.galerio.ui.components.VideoCard
import com.example.galerio.utils.MediaUtils.getDeviceMedia
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función para mostrar la lista de imágenes en una cuadrícula
@Composable
fun MediaList(modifier: Modifier, context: Context, onImageClick: (Uri) -> Unit) {
    val mediaItems = getDeviceMedia(context)

    // Agrupamos por fecha (utilizando la fecha tomada del media)
    val groupedMediaItems = mediaItems.groupBy { (_, _, dateTaken) ->
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        dateFormat.format(Date(dateTaken))
    }

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

            items(mediaForDate) { mediaItem ->
                when (mediaItem.type) {
                    MediaType.Image -> ImageCard(imageUri = mediaItem.uri, onClick = { onImageClick(mediaItem.uri) })
                    MediaType.Video -> VideoCard(videoUri = mediaItem.uri, duration = mediaItem.duration, onClick = { onImageClick(mediaItem.uri) })
                }
            }
        }
    }

}