package com.example.galerio.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.galerio.data.model.MediaItem
import com.example.galerio.data.service.PhotoService

class PhotoRepository {
    private val photoService = PhotoService()
    private val _photos = MutableLiveData<List<MediaItem>>()
    val photos: LiveData<List<MediaItem>> = _photos

    fun loadPhotos() {
        // Lógica para cargar las fotos desde el servicio web y actualizar _photos
    }
}