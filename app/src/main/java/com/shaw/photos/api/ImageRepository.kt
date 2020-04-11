package com.shaw.photos.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRepository {
    private var client: SimpleService = simpleService

    fun getList(): LiveData<List<Image>> {
        val liveData = MutableLiveData<List<Image>>()

        client.getList().enqueue(object: Callback<List<Image>> {
            override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                if (response.isSuccessful) {

                    liveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // Synchronously return LiveData
        // Its value will be available onResponse
        return liveData
    }
}