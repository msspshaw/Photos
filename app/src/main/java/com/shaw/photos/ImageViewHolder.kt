package com.shaw.photos

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shaw.photos.api.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image.view.*

class ImageViewHolder(inputView: View) : RecyclerView.ViewHolder(inputView), View.OnClickListener {
    private val logTag = "ImageViewHolder"
    private val missingImageError = "Image is not bound to view holder"
    private val missingImageListener = "Image Listener is not bound properly to view holder"

    private var image: Image? = null
    private var imageListener: ImageListener? = null
    private val view = inputView
    init {
        inputView.setOnClickListener(this)
    }

    override fun onClick(clickedView: View) {
        if (image == null) {
            Log.e(logTag, missingImageError)
            return
        }
        if (imageListener == null) {
            Log.e(logTag, missingImageListener)
            return
        }
        imageListener?.openFullImage(image?.download_url ?: "")
    }

    fun bindImage(image: Image, imageListener: ImageListener) {
        this.image = image
        this.imageListener = imageListener
        // Note that we aren't explicitly handling caching here as Picasso will do caching for us
        // https://square.github.io/picasso/2.x/picasso/com/squareup/picasso/Picasso.html#with-android.content.Context- (check get method note)
        Picasso.get().load(image.download_url).fit().centerCrop().into(view.image)
        view.author.text = image.author
    }

}