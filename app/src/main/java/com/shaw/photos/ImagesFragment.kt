package com.shaw.photos

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.images_fragment.*

class ImagesFragment: Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter
    private lateinit var listener: ImageListener

    private val portraitColumnCount = 2
    private val landscapeColumnCount = 3

    private val listenerError = "Error: Activity launching ImagesFragment does not implement the needed ImageListener interface"
    val classTag  = "ImagesFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> gridLayoutManager = GridLayoutManager(activity, portraitColumnCount)
            Configuration.ORIENTATION_LANDSCAPE -> gridLayoutManager = GridLayoutManager(activity, landscapeColumnCount)
        }
        return inflater.inflate(R.layout.images_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showLoading()
        loadData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ImageListener
        } catch (castException: ClassCastException) {
            Log.e(classTag, listenerError)
        }

    }

    private fun showLoading() {
        // Don't show loading if data is already loaded
        if (viewModel.getImages().value != null) {
            loading.visibility = View.VISIBLE
            imagesRecycler.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        imagesRecycler.visibility = View.VISIBLE
    }

    private fun loadData() {
        viewModel.getImages().observe(this, Observer {
            hideLoading()
            val list = it

            //Grid Layout Manager set in onCreateView to handle orientation shifts
            imageRecyclerAdapter = ImageRecyclerAdapter(list, listener)
            imagesRecycler.adapter = imageRecyclerAdapter
            imagesRecycler.layoutManager = gridLayoutManager
        })
    }
}