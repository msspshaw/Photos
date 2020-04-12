package com.shaw.photos.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val simpleService: SimpleService by lazy {
    Retrofit.Builder()
        .baseUrl("https://picsum.photos/v2/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(SimpleService::class.java)
}