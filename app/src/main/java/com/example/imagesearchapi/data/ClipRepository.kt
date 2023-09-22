package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.ClipSearchResponse
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    fun getClipList(query: String, sort: String) : Flow<ApiState<ClipSearchResponse>>
}
