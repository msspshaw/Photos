package com.shaw.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

interface ImageListener {
    fun openFullImage(downloadUrl: String)
    fun openLink(url: String)
}

class MainActivity : AppCompatActivity(), ImageListener {
    private val logTag = "MainActivity"
    private val imageDownloadUrlMissingError = "The image download url is missing for the image that was clicked"
    private val imageWebUrlMissingError = "The image web url is missing for the image that was clicked"

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

    override fun openFullImage (downloadUrl: String) {
        if (downloadUrl == "") {
            Log.e(logTag, imageDownloadUrlMissingError)
            return
        }
        val fullImageFragment = fullImageNewInstance(downloadUrl)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
            .addToBackStack(fullImageClassTag)
            .replace(R.id.fragmentContainer, fullImageFragment).commit()
    }

    override fun openLink (url: String) {
        if (url == "") {
            Log.e(logTag, imageWebUrlMissingError)
            return
        }
        val infoFragment = infoFragmentNewInstance(url)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
            .addToBackStack(infoFragmentClassTag)
            .replace(R.id.fragmentContainer, infoFragment).commit()
    }
}
