package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.ImageSearchResponse
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImageList(query: String, sort: String) : Flow<ApiState<ImageSearchResponse>>
}