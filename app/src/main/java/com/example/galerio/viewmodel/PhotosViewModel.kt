package com.example.galerio.viewmodel

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galerio.data.model.MediaItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotosViewModel : ViewModel() {
    private val _photos = MutableLiveData<Map<String, List<MediaItem>>>()
    val photos: LiveData<Map<String, List<MediaItem>>> = _photos

    fun loadPhotos(context: Context) {
    }
}