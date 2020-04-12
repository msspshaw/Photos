package com.shaw.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaw.photos.api.Image

class ImageRecyclerAdapter (private val images: List<Image>): RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return ImageViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = images.get(position)
        holder.bindImage(currentImage)
    }

}