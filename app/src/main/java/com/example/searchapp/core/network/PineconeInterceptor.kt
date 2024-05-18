package com.example.searchapp.core.network

import okhttp3.Interceptor
import okhttp3.Response

class PineconeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Api-Key",
                "343074e6-f3c9-4e22-8452-e03097cbc584"
            )
            .addHeader(
                "Content-Type",
                "application/json"
            )
            .build()
        return chain.proceed(request)
    }
}