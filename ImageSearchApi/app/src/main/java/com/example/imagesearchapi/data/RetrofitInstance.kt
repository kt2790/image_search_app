package com.example.imagesearchapi.data

import com.example.imagesearchapi.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : HttpApi by lazy {
        retrofit.create(HttpApi::class.java)
    }
}