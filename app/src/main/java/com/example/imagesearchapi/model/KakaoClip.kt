package com.example.imagesearchapi.model

data class KakaoClip (
    val title : String,
    val thumbnail : String,
    val datetime : String
)

fun KakaoClip.asPresModel(kakaoClip: KakaoClip) =
    PresModel("[Clip] " + kakaoClip.title, kakaoClip.thumbnail, kakaoClip.datetime.substring(0 until 10))