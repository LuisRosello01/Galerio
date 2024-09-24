package com.example.galerio.viewmodel

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galerio.data.model.Photo
import com.example.galerio.data.repository.PhotoRepository

class PhotosViewModel : ViewModel() {
    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> = _photos

    fun loadPhotos(context: Context) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val photos = mutableListOf<Photo>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                val id = cursor.getInt(idColumnIndex)
                val path = cursor.getString(dataColumnIndex)

                photos.add(Photo(title = id.toString(), uri = path, dateTaken = System.currentTimeMillis()))
            }
            cursor.close()
        }

        _photos.value = photos
    }
}