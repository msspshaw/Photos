package com.shaw.photos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shaw.photos.api.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image.view.*

class ImageViewHolder(inputView: View) : RecyclerView.ViewHolder(inputView), View.OnClickListener {
    private var image: Image? = null
    private val view = inputView
    init {
        inputView.setOnClickListener(this)
    }

    override fun onClick(clickedView: View) {
        System.out.println("View clicked, id: " + clickedView.id)
    }

    fun bindImage(image: Image) {
        this.image = image
        // Note that we aren't explicitly handling caching here as Picasso will do caching for us
        // https://square.github.io/picasso/2.x/picasso/com/squareup/picasso/Picasso.html#with-android.content.Context- (check get method note)
        Picasso.get().load(image.download_url).fit().centerCrop().into(view.image)
        view.author.text = image.author
    }

}