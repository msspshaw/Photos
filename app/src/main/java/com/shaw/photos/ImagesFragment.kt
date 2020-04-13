package com.shaw.photos

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.shaw.photos.api.Image
import kotlinx.android.synthetic.main.images_fragment.*

class ImagesFragment: Fragment(), View.OnClickListener {
    private var viewModel: MainViewModel? = null
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter
    private lateinit var listener: ImageListener
    private var images: LiveData<List<Image>>? = null
    private var savedScroll:Int? = null

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
        sortByAuthorButton.setOnClickListener(this)
        sortBySizeButton.setOnClickListener(this)
        // Save this value to check if we should scroll to saved location
        var dataPersisted = images?.value != null

        if (!dataPersisted) {
            Log.d(classTag, "View Model was null")
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            images = viewModel?.getImages()
            showLoading()
        }
        images?.observe(this, Observer {
            hideLoading()
            //Grid Layout Manager set in onCreateView to handle orientation shifts
            imageRecyclerAdapter = ImageRecyclerAdapter(it, listener)
            imagesRecycler.adapter = imageRecyclerAdapter
            imagesRecycler.layoutManager = gridLayoutManager
            sortBar.visibility = View.VISIBLE

            if (dataPersisted && savedScroll != null) {
                dataPersisted = false
                gridLayoutManager.scrollToPosition(savedScroll ?: 0)
            }
        })
    }

    override fun onDestroyView() {
        Log.d(classTag, "Entering on destory view, scroll position: " + gridLayoutManager.findFirstVisibleItemPosition().toString())
        savedScroll = gridLayoutManager.findFirstVisibleItemPosition()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ImageListener
        } catch (castException: ClassCastException) {
            Log.e(classTag, listenerError)
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.sortByAuthorButton -> {
                Log.d(classTag, "Sort by Author button Clicked")
                sortByAuthorClicked()
            }
            R.id.sortBySizeButton -> {
                Log.d(classTag, "Sort by Size button Clicked")
                sortBySizeClicked()
            }
        }
    }

    private fun showLoading() {
        // Don't show loading if data is already loaded
        if (viewModel?.getImages()?.value == null) {
            loading.visibility = View.VISIBLE
            imagesRecycler.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        imagesRecycler.visibility = View.VISIBLE
    }

    private var authorSortSelected = false
    private var authorSortAscending = true

    private var sizeSortSelected = false
    private var sizeSortAscending = true

    private fun sortByAuthorClicked() {
        // If this sort type was already selected, reverse direction
        if (authorSortSelected) {
            authorSortAscending = !authorSortAscending
            if (authorSortAscending) setArrowDown(sortByAuthorButton) else setArrowUp(sortByAuthorButton)
        }

        // Ensure this sort selected, others deselected
        authorSortSelected = true
        sortByAuthorButton.setBackgroundResource(R.drawable.selected_background)
        sizeSortSelected = false
        sortBySizeButton.setBackgroundResource(R.color.transparent)

        viewModel?.sortImagesByAuthor(authorSortAscending)
    }

    private fun sortBySizeClicked() {
        // If this sort type was already selected, reverse direction
        if (sizeSortSelected) {
            sizeSortAscending = !sizeSortAscending
            if (sizeSortAscending) setArrowDown(sortBySizeButton) else setArrowUp(sortBySizeButton)
        }

        // Ensure this sort selected, others deselected
        sizeSortSelected = true
        sortBySizeButton.setBackgroundResource(R.drawable.selected_background)
        authorSortSelected = false
        sortByAuthorButton.setBackgroundResource(R.color.transparent)

        viewModel?.sortImagesBySize(sizeSortAscending)
    }

    private fun setArrowUp (button: Button) {
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sized_up_arrow, 0)
    }

    private fun setArrowDown (button: Button) {
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sized_down_arrow, 0)
    }
}