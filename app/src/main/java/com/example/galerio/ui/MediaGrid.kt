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
import com.example.galerio.ui.components.ImageCard
import com.example.galerio.utils.MediaUtils.getDeviceImages
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función para mostrar la lista de imágenes en una cuadrícula
@Composable
fun MediaList(modifier: Modifier, context: Context, onImageClick: (Uri) -> Unit) {
    val images = getDeviceImages(context)

    val groupedImages = images.groupBy { (_, dateTaken) ->
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        dateFormat.format(Date(dateTaken))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        groupedImages.forEach { (date, imagesForDate) ->
            // StickyHeader o cabecera
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }

            items(imagesForDate) { (imageUri, _) ->
                ImageCard(imageUri = imageUri, onClick = { onImageClick(imageUri) })
            }
        }
    }

}