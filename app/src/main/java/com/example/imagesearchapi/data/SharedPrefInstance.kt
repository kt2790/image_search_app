package com.example.imagesearchapi.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.imagesearchapi.MyApplication
import com.example.imagesearchapi.model.PresModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SharedPrefInstance private constructor() {
    private val application = MyApplication.instance
    private val prefs: SharedPreferences by lazy { application.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE) }
    private val editor: SharedPreferences.Editor by lazy { prefs.edit() }

    suspend fun setBookmarkListPref(values: List<PresModel>) {
        withContext(Dispatchers.IO) {
            val gson = Gson()
            val json = gson.toJson(values)
            editor.putString(PREF_KEY, json)
            editor.apply()
        }
    }

    suspend fun addBookmarkPref(value: PresModel) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString(PREF_KEY, null)
            val gson = Gson()

            val storedData : MutableList<PresModel> = gson.fromJson(json, object : TypeToken<MutableList<PresModel>?>() {}.type) ?: mutableListOf()

            if (storedData.none { it.image_url == value.image_url && it.sitename == value.sitename }) {
                storedData.add(value)

                editor.putString(PREF_KEY, gson.toJson(storedData))
                editor.apply()
            }
        }
    }

    suspend fun deleteBookmarkPref(value: PresModel) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString(PREF_KEY, null)
            val gson = Gson()

            val storedData : MutableList<PresModel> = gson.fromJson(json, object : TypeToken<MutableList<PresModel>?>() {}.type) ?: mutableListOf()
            storedData.remove(storedData.find { it.image_url == value.image_url && it.sitename == value.sitename })

            editor.putString(PREF_KEY, gson.toJson(storedData))
            editor.apply()
        }
    }

    fun getBookmarkList() = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            val json = sharedPreferences.getString(key, null)
            val gson = Gson()

            val storedData : List<PresModel> = gson.fromJson(json, object : TypeToken<List<PresModel>?>() {}.type) ?: mutableListOf()
            trySend(storedData)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)

        val json = prefs.getString(PREF_KEY, null)
        val gson = Gson()

        val storedData : List<PresModel> = gson.fromJson(json, object : TypeToken<List<PresModel>?>() {}.type) ?: mutableListOf()
        send(storedData)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.flowOn(Dispatchers.IO).buffer(Channel.UNLIMITED)

    companion object {
        private const val PREF_KEY = "BOOKMARK"
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