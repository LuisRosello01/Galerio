package com.example.galerio

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galerio.data.model.Photo
import java.io.File

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)

    fun bind(context: Context, photo: Photo) {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(photo.uri))
        val outputFile = File(context.filesDir, "${photo.title}.jpg")
        inputStream?.copyTo(outputFile.outputStream())
        Glide.with(itemView.context).load(outputFile).into(imageView)
    }
}