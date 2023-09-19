package com.example.imagesearchapi.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagesearchapi.data.BookmarkRepositoryImpl
import com.example.imagesearchapi.data.ImageRepositoryImpl

class ContentViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            return ContentViewModel(ImageRepositoryImpl(), BookmarkRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}