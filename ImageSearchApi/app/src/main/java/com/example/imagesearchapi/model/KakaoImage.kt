package com.example.imagesearchapi.model

import com.google.gson.annotations.SerializedName

data class KakaoImage(
    @SerializedName("display_sitename")
    val sitename : String,
    val collection : String,
    val image_url : String,
    val datetime : String
)