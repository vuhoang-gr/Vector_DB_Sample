package com.example.searchapp.core.common

import android.util.Log
import com.example.searchapp.core.initializer.ContextProvider
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder.TextEmbedderOptions

class TextEmbedderHelper(
    private var currentDelegate: Int = DELEGATE_CPU,
    private var currentModel: Int = MODEL_MOBILE_BERT,
) {
    private var textEmbedder: TextEmbedder? = null

    init {
        setupTextEmbedder()
    }

    private fun setupTextEmbedder() {
        val baseOptionsBuilder = BaseOptions.builder()
        when (currentDelegate) {
            DELEGATE_CPU -> {
                baseOptionsBuilder.setDelegate(Delegate.CPU)
            }
            DELEGATE_GPU -> {
                baseOptionsBuilder.setDelegate(Delegate.GPU)
            }
        }
        when (currentModel) {
            MODEL_MOBILE_BERT -> {
                baseOptionsBuilder.setModelAssetPath(MODEL_MOBILE_BERT_PATH)
            }
            MODEL_AVERAGE_WORD -> {
                baseOptionsBuilder.setModelAssetPath(MODEL_AVERAGE_WORD_PATH)
            }
        }
        try {
            val baseOptions = baseOptionsBuilder.build()
            val optionsBuilder =
                TextEmbedderOptions.builder().setBaseOptions(baseOptions)
            val options = optionsBuilder.build()
            textEmbedder = TextEmbedder.createFromOptions(ContextProvider.getAppContext(), options)
        } catch (e: IllegalStateException) {
            Log.e(
                TAG,
                "Text embedder failed to load model with error: " + e.message
            )
        } catch (e: RuntimeException) {
            Log.e(
                TAG,
                "Text embedder failed to load model with error: " + e.message
            )
        }
    }

    fun createEmbeddedArray(text: String): List<Float> {
        textEmbedder?.let {
            return it.embed(text).embeddingResult().embeddings().first().floatEmbedding().toList()
        }
        return emptyList()
    }

    fun clearTextEmbedder() {
        textEmbedder?.close()
        textEmbedder = null
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val MODEL_MOBILE_BERT = 0
        const val MODEL_AVERAGE_WORD = 1
        const val MODEL_MOBILE_BERT_PATH = "universal_sentence_encoder.tflite"
        const val MODEL_AVERAGE_WORD_PATH = "universal_sentence_encoder.tflite"
        private const val TAG = "TextEmbedderHelper"

        val instance: TextEmbedderHelper = TextEmbedderHelper()
    }
}