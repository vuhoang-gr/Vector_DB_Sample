package com.example.searchapp.core.initializer

import android.content.Context
import androidx.startup.Initializer

class ContextInitializer : Initializer<Boolean> {
    override fun create(context: Context): Boolean {
        ContextProvider.applicationContext = context.applicationContext
        return true
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}