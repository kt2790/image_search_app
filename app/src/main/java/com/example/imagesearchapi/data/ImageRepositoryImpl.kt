package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.ImageSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class ImageRepositoryImpl : ImageRepository {
    override fun getImageList(query : String, sort : String): Flow<ApiState<ImageSearchResponse>> = flow {
        try {
            val response = RetrofitInstance.api.searchImage(query = query, sort = sort, page = 1, size = 5)

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiState.Success(it))
                }
            } else {
                try {
                    emit(ApiState.Error(response.errorBody()!!.string()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: ""))
        } as Unit
    }
}