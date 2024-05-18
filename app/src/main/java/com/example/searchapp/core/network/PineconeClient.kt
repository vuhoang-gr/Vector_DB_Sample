package com.example.searchapp.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object PineconeClient {
    private fun provideClient(baseUrl: String): PineconeApi {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(PineconeInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(PineconeApi::class.java)
    }

    val image: PineconeApi = provideClient("https://image-search-9vdh0h0.svc.aped-4627-b74a.pinecone.io")

    val text: PineconeApi = provideClient("https://text-search-9vdh0h0.svc.aped-4627-b74a.pinecone.io")
}