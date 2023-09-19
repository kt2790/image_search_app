package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.KakaoImage
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun setBookmarkListPref(values: List<KakaoImage>)

    fun getBookmarkListPref() : Flow<List<KakaoImage>>

    suspend fun addBookmarkPref(value: KakaoImage)

    suspend fun deleteBookmarkPref(value: KakaoImage)
}