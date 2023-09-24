package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.PresModel
import kotlinx.coroutines.flow.Flow

class BookmarkRepositoryImpl : BookmarkRepository {
    private val sharedPrefInstance = SharedPrefInstance.getInstance()

    override suspend fun setBookmarkListPref(values: List<PresModel>) {
        sharedPrefInstance.setBookmarkListPref(values)
    }

    override fun getBookmarkList(): Flow<List<PresModel>> {
        return sharedPrefInstance.getBookmarkList()
    }

    override suspend fun addBookmarkPref(value: PresModel) {
        sharedPrefInstance.addBookmarkPref(value)
    }

    override suspend fun deleteBookmarkPref(value: PresModel) {
        sharedPrefInstance.deleteBookmarkPref(value)
    }
}