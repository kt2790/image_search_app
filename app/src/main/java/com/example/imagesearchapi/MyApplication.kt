package com.example.imagesearchapi

import android.app.Application

class MyApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}