/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.searchapp.core.common

import android.graphics.Bitmap
import android.util.Log
import com.example.searchapp.core.initializer.ContextProvider
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder
import com.google.mediapipe.tasks.vision.imageembedder.ImageEmbedder.ImageEmbedderOptions

class ImageEmbedderHelper(
    private var currentDelegate: Int = DELEGATE_CPU,
    private var currentModel: Int = MODEL_SMALL
) {
    private var imageEmbedder: ImageEmbedder? = null

    init {
        setupImageEmbedder()
    }

    private fun setupImageEmbedder() {
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
            MODEL_SMALL -> {
                baseOptionsBuilder.setModelAssetPath(MODEL_SMALL_PATH)
            }
            MODEL_LARGE -> {
                baseOptionsBuilder.setModelAssetPath(MODEL_LARGE_PATH)
            }
        }
        try {
            val baseOptions = baseOptionsBuilder.build()
            val optionsBuilder =
                ImageEmbedderOptions.builder().setBaseOptions(baseOptions)
            val options = optionsBuilder.build()
            imageEmbedder = ImageEmbedder.createFromOptions(ContextProvider.getAppContext(), options)
        } catch (e: IllegalStateException) {
            Log.d(
                TAG,
                "Image embedder failed to load model with error: " + e.message
            )
        } catch (e: RuntimeException) {
            Log.d(
                TAG,
                "Image embedder failed to load model with error: " + e.message
            )
        }
    }

    fun createEmbeddedArray(image: Bitmap): List<Float> {
        val mpImage = BitmapImageBuilder(image).build()
        imageEmbedder?.let {
            return it.embed(mpImage).embeddingResult().embeddings().first().floatEmbedding().toList()
        }
        return emptyList()
    }

    fun clearImageEmbedder() {
        imageEmbedder?.close()
        imageEmbedder = null
    }

    companion object {
        private const val DELEGATE_CPU = 0
        private const val DELEGATE_GPU = 1
        private const val MODEL_SMALL = 0
        private const val MODEL_LARGE = 1
        private const val MODEL_SMALL_PATH = "mobilenet_v3_small.tflite"
        private const val MODEL_LARGE_PATH = "mobilenet_v3_large.tflite"
        private const val TAG = "ImageEmbedderHelper"

        val instance: ImageEmbedderHelper = ImageEmbedderHelper()
    }
}
