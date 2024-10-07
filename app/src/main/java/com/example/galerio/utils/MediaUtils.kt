package com.example.galerio.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.galerio.data.model.MediaItem
import com.example.galerio.data.model.MediaType

object MediaUtils {
    fun getDeviceMedia(context: Context): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()

        val imageProjection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.RELATIVE_PATH)
        val videoProjection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DURATION)

//        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val imageQuery = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        imageQuery?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = cursor.getLong(dateColumn)
                val relativePath = cursor.getString(path)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                mediaList.add(MediaItem(uri, MediaType.Image, dateTaken, relativePath))
            }
        }

        val videoQuery = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_TAKEN} DESC"
        )

        // Procesar los videos
        videoQuery?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = cursor.getLong(dateTakenColumn)
                val duration = cursor.getLong(durationColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                mediaList.add(MediaItem(uri, MediaType.Video, dateTaken, null, duration))
            }
        }

        return mediaList
    }
}
