package com.shaw.photos.api

import retrofit2.Call
import retrofit2.http.GET

interface SimpleService {
    @GET("list/")
    fun getList(): Call<List<Image>>
}