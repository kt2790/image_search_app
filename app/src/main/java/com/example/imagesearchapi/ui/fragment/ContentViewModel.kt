package com.example.imagesearchapi.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesearchapi.data.ApiState
import com.example.imagesearchapi.data.BookmarkRepository
import com.example.imagesearchapi.data.ClipRepository
import com.example.imagesearchapi.data.ImageRepository
import com.example.imagesearchapi.model.ImageSearchResponse
import com.example.imagesearchapi.model.PresModel
import com.example.imagesearchapi.model.asPresModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed interface UiState<T> {
    class Loading<T> : UiState<T>
    data class Success<T> (val data: T?) : UiState<T>
    data class Error<T> (val message: String) : UiState<T>
}

class ContentViewModel (private val imageRepository : ImageRepository, private val clipRepository: ClipRepository, private val bookmarkRepository: BookmarkRepository) : ViewModel() {
    private val _searchResult : MutableLiveData<UiState<List<PresModel>>> = MutableLiveData(UiState.Loading())
    val searchResult : LiveData<UiState<List<PresModel>>>
        get() = _searchResult

    private val _bookmarkList = MutableLiveData<List<PresModel>>()
    val bookmarkList : LiveData<List<PresModel>>
        get() = _bookmarkList

    init {
        getBookmarkListPref()
    }

    fun getContentList(query: String, sort: String) {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading()
            imageRepository.getImageList(query, sort)
                .combine (clipRepository.getClipList(query, sort)) { image, clip ->
                    image.data?.documents?.map { it.asPresModel(it) }?.toMutableList()?.apply {
                        addAll(clip.data?.documents?.map { item -> item.asPresModel(item) } ?: mutableListOf())
                    }
                }
                .catch { error ->
                    _searchResult.value = UiState.Error("${error.message}")
                }
                .collect { values ->
                    _searchResult.value = UiState.Success(values)
                }
        }
    }



    private fun getBookmarkListPref() {
        viewModelScope.launch {
            bookmarkRepository.getBookmarkListPref().collect {
                _bookmarkList.value = it
            }
        }
    }

    fun addBookmarkPref(value: PresModel) {
        viewModelScope.launch {
            bookmarkRepository.addBookmarkPref(value)
            bookmarkRepository.getBookmarkListPref().collect {
                _bookmarkList.value = it
            }
        }
    }

    fun deleteBookmarkPref(value: PresModel) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmarkPref(value)
            bookmarkRepository.getBookmarkListPref().collect {
                _bookmarkList.value = it
            }
        }
    }


}