package com.example.imagesearchapi.data

import android.content.Context
import android.content.SharedPreferences
import com.example.imagesearchapi.MyApplication
import com.example.imagesearchapi.model.KakaoImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SharedPrefInstance private constructor() {
    private val application = MyApplication.instance
    private val prefs: SharedPreferences by lazy { application.getSharedPreferences("BOOKMARK", Context.MODE_PRIVATE) }
    private val editor: SharedPreferences.Editor by lazy { prefs.edit() }

    suspend fun setBookmarkListPref(values: List<KakaoImage>) {
        withContext(Dispatchers.IO) {
            val gson = Gson()
            val json = gson.toJson(values)
            editor.putString("BOOKMARK", json)
            editor.apply()
        }
    }

    suspend fun addBookmarkPref(value: KakaoImage) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString("BOOKMARK", null)
            val gson = Gson()

            val storedData : MutableList<KakaoImage> = gson.fromJson(json, object : TypeToken<MutableList<KakaoImage>?>() {}.type) ?: mutableListOf()

            if (storedData.none { it.image_url == value.image_url && it.sitename == value.sitename }) {
                storedData.add(value)

                editor.putString("BOOKMARK", gson.toJson(storedData))
                editor.apply()
            }
        }
    }

    suspend fun deleteBookmarkPref(value: KakaoImage) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString("BOOKMARK", null)
            val gson = Gson()

            val storedData : MutableList<KakaoImage> = gson.fromJson(json, object : TypeToken<MutableList<KakaoImage>?>() {}.type) ?: mutableListOf()
            storedData.remove(storedData.find { it.image_url == value.image_url && it.sitename == value.sitename })

            editor.putString("BOOKMARK", gson.toJson(storedData))
            editor.apply()
        }
    }

    fun getBookmarkListPref() = flow {
        val json = prefs.getString("BOOKMARK", null)
        val gson = Gson()

        val storedData : List<KakaoImage> = gson.fromJson(json, object : TypeToken<List<KakaoImage>?>() {}.type) ?: mutableListOf()
        emit(storedData)
    }.flowOn(Dispatchers.IO)

    companion object {
        private var instance: SharedPrefInstance? = null

        fun getInstance() : SharedPrefInstance {
            return instance ?: synchronized(this) {
                instance ?: SharedPrefInstance().also {
                    instance = it
                }
            }
        }
    }
}