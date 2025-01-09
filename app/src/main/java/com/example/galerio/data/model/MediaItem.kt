package com.example.galerio.data.model

import android.net.Uri

data class MediaItem(
    val uri: Uri,
    val type: MediaType,
    val dateModified: Long,
    val relativePath: String? = null,
    val duration: Long? = null // Solo para videos
)
