package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.KakaoImage
import kotlinx.coroutines.flow.Flow

class BookmarkRepositoryImpl : BookmarkRepository {
    private val sharedPrefInstance = SharedPrefInstance.getInstance()

    override suspend fun setBookmarkListPref(values: List<KakaoImage>) {
        sharedPrefInstance.setBookmarkListPref(values)
    }

    override fun getBookmarkListPref() : Flow<List<KakaoImage>> {
        return sharedPrefInstance.getBookmarkListPref()
    }

    override suspend fun addBookmarkPref(value: KakaoImage) {
        sharedPrefInstance.addBookmarkPref(value)
    }

    override suspend fun deleteBookmarkPref(value: KakaoImage) {
        sharedPrefInstance.deleteBookmarkPref(value)
    }
}