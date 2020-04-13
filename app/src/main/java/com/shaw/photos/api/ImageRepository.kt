package com.shaw.photos.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRepository {
    private var client: SimpleService = simpleService
    private val images = MutableLiveData<List<Image>>()

    private val classTag = "ImageRepository"
    private val sortingEmptyError = "Sorting was attempted on empty repository"

    fun getList(): LiveData<List<Image>> {
        client.getList().enqueue(object: Callback<List<Image>> {
            override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                if (response.isSuccessful) {
                    response.body().forEach {
                        it.totalSize = it.width * it.height
                    }
                    images.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                // Realistically this would involve analytics and additional error messaging
                t.printStackTrace()
            }
        })

        return images
    }

    fun sortListBySize (ascending: Boolean) {
        // This should always be called after we have gotten a response from getList
        if (images.value?.isEmpty() != false){
            Log.e(classTag, sortingEmptyError)
            return
        }
        else {
            // Since images.value is mutable, the compiler cannot guarantee that it will be
            // populated at the time of this call (although it is checked above) - in this case
            // we will simply pass an empty list if images.value is null at the time of the call
            val imageList = quickSortBySize(images.value ?: mutableListOf())

            if (ascending) images.value = imageList else images.value = imageList.reversed()
        }
    }

    private fun quickSortBySize(imageList: List<Image>): List<Image> {
        // Exit case for recursion
        if (imageList.count() <= 1) return imageList

        // For this sorting method, any pivot can be chosen - we're choosing the (roughly) middle item
        val pivot = imageList[imageList.count()/2]

        // This is a slightly quicker, slightly less efficient quick sort method than sort in place
        // In my opinion, unless efficiency is totally required, the readability is much higher using this method
        val equal = imageList.filter { it.totalSize == pivot.totalSize }
        val less = imageList.filter { it.totalSize < pivot.totalSize }
        val greater = imageList.filter { it.totalSize > pivot.totalSize }
        return quickSortBySize(less) + equal + quickSortBySize(greater)
    }

    fun sortListByAuthor (ascending: Boolean) {
        // This should always be called after we have gotten a response from getList
        if (images.value?.isEmpty() != false){
            Log.e(classTag, sortingEmptyError)
            return
        }
        else {
            // Since images.value is mutable, the compiler cannot guarantee that it will be
            // populated at the time of this call (although it is checked above) - in this case
            // we will simply pass an empty list if images.value is null at the time of the call
            val imageList = quickSortByAuthor(images.value ?: mutableListOf())

            if (ascending) images.value = imageList else images.value = imageList.reversed()
        }
    }

    private fun quickSortByAuthor(imageList: List<Image>): List<Image> {
        // Exit case for recursion
        if (imageList.count() <= 1) return imageList

        // For this sorting method, any pivot can be chosen - we're choosing the (roughly) middle item
        val pivot = imageList[imageList.count()/2]

        // This is a slightly quicker, slightly less efficient quick sort method than sort in place
        // In my opinion, unless efficiency is totally required, the readability is much higher using this method
        val equal = imageList.filter { it.author == pivot.author }
        val less = imageList.filter { it.author < pivot.author }
        val greater = imageList.filter { it.author > pivot.author }
        return quickSortByAuthor(less) + equal + quickSortByAuthor(greater)
    }
}