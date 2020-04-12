package com.shaw.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.full_image.*
import android.util.Log

val fullImageClassTag = "FullImageFragment"
private val newInstanceMissingError = "FullImageFragment was not instantiated with newInstance(), and cannot load an image"
private val urlTag = "url"

fun fullImageNewInstance(imageUrl: String): FullImageFragment {
    val fragment = FullImageFragment()
    val args = Bundle()
    args.putString(urlTag, imageUrl)
    fragment.arguments = args
    return fragment
}

class FullImageFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.full_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = arguments?.getString(urlTag)
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).fit().centerInside().into(fullImageView)
        }
        else {
            Log.e(fullImageClassTag, newInstanceMissingError)
        }
    }
}