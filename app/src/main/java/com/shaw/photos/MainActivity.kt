package com.shaw.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

interface ImageListener {
    fun openFullImage(url: String)
    fun openAuthorLink(authorUrl: String)
}

class MainActivity : AppCompatActivity(), ImageListener {
    private val logTag = "MainActivity"
    private val imageUrlMissingError = "The image url is missing for the image that was clicked"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Verify that the fragment container does not currently contain a fragment, so that we do not
        // overwrite another fragment with the main list
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            val imagesFragment = ImagesFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
                .addToBackStack(imagesFragment.classTag)
                .replace(R.id.fragmentContainer, imagesFragment).commit()
        }
    }

    override fun openFullImage (url: String) {
        if (url == "") {
            Log.e(logTag, imageUrlMissingError)
            return
        }
        val fullImageFragment = fullImageNewInstance(url)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
            .addToBackStack(fullImageClassTag)
            .replace(R.id.fragmentContainer, fullImageFragment).commit()
    }

    override fun openAuthorLink (authorUrl: String) {

    }
}
