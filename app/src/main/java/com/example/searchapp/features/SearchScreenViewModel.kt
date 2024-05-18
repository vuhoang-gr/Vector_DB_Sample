package com.example.searchapp.features

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchapp.core.common.ImageEmbedderHelper
import com.example.searchapp.core.common.TextEmbedderHelper
import com.example.searchapp.core.model.PineconeQuery
import com.example.searchapp.core.network.PineconeClient
import com.example.searchapp.core.network.dto.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel : ViewModel() {
    private val _currentText = MutableStateFlow<String>("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    var similarText: String by mutableStateOf("")
        private set

    var currentImage: Bitmap? by mutableStateOf(null)
        private set

    var similarImages: List<Match> by mutableStateOf(emptyList())
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    private val imageApi = PineconeClient.image

    private val textApi = PineconeClient.text

    fun onChooseImage(bitmap: Bitmap) {
        currentImage = bitmap
        val embeddedVector = ImageEmbedderHelper.instance.createEmbeddedArray(bitmap)

        viewModelScope.launch {
            try {
                isLoading = true
                val queries = imageApi.query(
                    PineconeQuery(
                        namespace = "",
                        vector = embeddedVector,
                        topK = 10,
                        includeValues = false,
                        includeMetadata = true
                    )
                )
                similarImages = queries.matches.filter {
                    it.score > 0.5 && it.score < 1
                }.map { it.copy(id = "images/${it.id}") }
                isLoading = false

            } catch (e: Exception) {
                Log.d(
                    "Search Error",
                    "try again"
                )
            }
        }
    }

    fun onChangeText(text: String) {
        viewModelScope.launch {
            _currentText.emit(text)
        }
    }

    fun onSearchText() {
        if (currentText.value.isBlank()) {
            return
        }
        val embeddedVector = TextEmbedderHelper.instance.createEmbeddedArray(currentText.value)

        viewModelScope.launch {
            try {
                isLoading = true
                val queries = textApi.query(
                    PineconeQuery(
                        namespace = "",
                        vector = embeddedVector,
                        topK = 1,
                        includeValues = false,
                        includeMetadata = true
                    )
                )

                similarText = queries.matches.first().metadata?.data.orEmpty()

                isLoading = false
            } catch (e: Exception) {
                Log.d(
                    "Search Error",
                    "try again"
                )
            }
        }
    }
}

