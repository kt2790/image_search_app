package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.PresModel
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun setBookmarkListPref(values: List<PresModel>)

    fun getBookmarkListPref() : Flow<List<PresModel>>

    suspend fun addBookmarkPref(value: PresModel)

    suspend fun deleteBookmarkPref(value: PresModel)
}