package com.example.imagesearchapi.model

import com.google.gson.annotations.SerializedName

data class KakaoImage(
    @SerializedName("display_sitename")
    val sitename : String,
    val collection : String,
    val image_url : String,
    val datetime : String
)

fun KakaoImage.asPresModel(kakaoImage: KakaoImage) =
    PresModel("[Image] " + kakaoImage.sitename, kakaoImage.image_url, kakaoImage.datetime.substring(0 until 10))


