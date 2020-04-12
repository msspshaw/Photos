package com.shaw.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter

    private val portraitColumnCount = 2
    private val landscapeColumnCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showLoading()
        loadData()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> gridLayoutManager = GridLayoutManager(this, portraitColumnCount)
            Configuration.ORIENTATION_LANDSCAPE -> gridLayoutManager = GridLayoutManager(this, landscapeColumnCount)
        }
        return super.onCreateView(name, context, attrs)
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        imagesRecycler.visibility = View.GONE
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
            imageRecyclerAdapter = ImageRecyclerAdapter(list)
            imagesRecycler.adapter = imageRecyclerAdapter
            imagesRecycler.layoutManager = gridLayoutManager
        })
    }
}
