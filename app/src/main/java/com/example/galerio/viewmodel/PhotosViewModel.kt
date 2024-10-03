package com.example.galerio.viewmodel

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galerio.data.model.Photo
import com.example.galerio.data.repository.PhotoRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotosViewModel : ViewModel() {
    private val _photos = MutableLiveData<Map<String, List<Photo>>>()
    val photos: LiveData<Map<String, List<Photo>>> = _photos

    fun loadPhotos(context: Context) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val photosByDate = mutableMapOf<String, MutableList<Photo>>()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val dateTakenColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                val id = cursor.getInt(idColumnIndex)
                val path = cursor.getString(dataColumnIndex)
                val dateTaken = cursor.getLong(dateTakenColumnIndex)

                val date = Date(dateTaken)
                val formattedDate = dateFormat.format(date)

                val photo = Photo(title = id, uri = path, dateTaken = dateTaken)

                photosByDate.getOrPut(formattedDate) { mutableListOf() }.add(photo)
            }
            cursor.close()
        }

        _photos.value = photosByDate
    }
}