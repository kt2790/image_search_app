package com.example.imagesearchapi.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.imagesearchapi.data.BookmarkRepository
import com.example.imagesearchapi.data.ClipRepository
import com.example.imagesearchapi.data.ImageRepository
import com.example.imagesearchapi.model.PresModel
import com.example.imagesearchapi.model.asPresModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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

    private val _bookmarkList by lazy {
        bookmarkRepository.getBookmarkList().asLiveData()
    }
    val bookmarkList : LiveData<List<PresModel>>
        get() = _bookmarkList

    private var page = 1
    private var curList : MutableList<PresModel>? = mutableListOf()
    private var currentQuery = ""
    private var currentSort = ""

    fun getContentList(query: String, sort: String) {
        page = 1
        currentQuery = query
        currentSort = sort

        viewModelScope.launch {
            _searchResult.value = UiState.Loading()
            imageRepository.getImageList(query, sort, page)
                .combine (clipRepository.getClipList(query, sort, page)) { image, clip ->
                    image.data?.documents?.map { it.asPresModel(it) }?.toMutableList()?.apply {
                        addAll(clip.data?.documents?.map { item -> item.asPresModel(item) } ?: mutableListOf())
                    }
                }
                .catch { error ->
                    _searchResult.value = UiState.Error("${error.message}")
                }
                .collect { values ->
                    curList = values
                    _searchResult.value = UiState.Success(curList)
                }
        }
    }

    fun getNextContent() {
        page++

        viewModelScope.launch {
            _searchResult.value = UiState.Loading()
            imageRepository.getImageList(currentQuery, currentSort, page)
                .combine (clipRepository.getClipList(currentQuery, currentSort, page)) { image, clip ->
                    image.data?.documents?.map { it.asPresModel(it) }?.toMutableList()?.apply {
                        addAll(clip.data?.documents?.map { item -> item.asPresModel(item) } ?: mutableListOf())
                    }
                }
                .catch { error ->
                    _searchResult.value = UiState.Error("${error.message}")
                }
                .collect { values ->
                    curList?.addAll(values ?: mutableListOf())
                    _searchResult.value = UiState.Success(curList)
                }
        }
    }

    fun addBookmarkPref(value: PresModel) {
        viewModelScope.launch {
            bookmarkRepository.addBookmarkPref(value)
        }
    }

    fun deleteBookmarkPref(value: PresModel) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmarkPref(value)
        }
    }
}