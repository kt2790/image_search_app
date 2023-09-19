package com.example.imagesearchapi.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesearchapi.data.ApiState
import com.example.imagesearchapi.data.BookmarkRepository
import com.example.imagesearchapi.data.ImageRepository
import com.example.imagesearchapi.model.ImageSearchResponse
import com.example.imagesearchapi.model.KakaoImage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ContentViewModel (private val imageRepository : ImageRepository, private val bookmarkRepository: BookmarkRepository) : ViewModel() {
    private val _searchResult : MutableLiveData<ApiState<ImageSearchResponse>> = MutableLiveData(ApiState.Loading())
    val searchResult : LiveData<ApiState<ImageSearchResponse>>
        get() = _searchResult

    private val _bookmarkList = MutableLiveData<List<KakaoImage>>()
    val bookmarkList : LiveData<List<KakaoImage>>
        get() = _bookmarkList

    fun getImageList(query: String, sort: String) {
        viewModelScope.launch {
            _searchResult.value = ApiState.Loading()
            imageRepository.getImageList(query, sort)
                .catch { error ->
                    _searchResult.value = ApiState.Error("${error.message}")
                }
                .collect { values ->
                    _searchResult.value = values
                }
        }
    }


    fun getBookmarkListPref() {
        viewModelScope.launch {
            val result = bookmarkRepository.getBookmarkListPref()

            result.collect {
                _bookmarkList.value = it
            }
        }
    }

    fun addBookmarkPref(value: KakaoImage) {
        viewModelScope.launch {
            bookmarkRepository.addBookmarkPref(value)
        }
        getBookmarkListPref()
    }

    fun deleteBookmarkPref(value: KakaoImage) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmarkPref(value)
        }
        getBookmarkListPref()
    }


}