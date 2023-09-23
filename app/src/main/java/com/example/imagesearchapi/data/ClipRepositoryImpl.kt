package com.example.imagesearchapi.data

import com.example.imagesearchapi.model.ClipSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ClipRepositoryImpl : ClipRepository {
    override fun getClipList(query: String, sort: String, page: Int): Flow<ApiState<ClipSearchResponse>> =
        flow {
            try {
                val response = RetrofitInstance.api.searchClip(query = query, sort = sort, page = page, size = 10)

                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(ApiState.Success(it))
                    }
                } else {
                    try {
                        emit(ApiState.Error(response.errorBody()!!.string()))
                    } catch (e: IOException) {
                        emit(ApiState.Error("IO Error"))
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                emit(ApiState.Error(e.message ?: ""))
            }
        }
}