package com.example.searchapp.core.initializer

import android.content.Context

object ContextProvider {
    lateinit var applicationContext: Context
    fun getAppContext(): Context {
        return applicationContext
    }
}