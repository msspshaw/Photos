package com.shaw.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shaw.photos.api.Image
import com.shaw.photos.api.ImageRepository

class MainViewModel : ViewModel() {
    private val repository: ImageRepository = ImageRepository()

    fun getImages(): LiveData<List<Image>> {
        return repository.getList()
    }
}