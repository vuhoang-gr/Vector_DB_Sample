package com.example.searchapp.core.network

import com.example.searchapp.core.model.PineconeQuery
import com.example.searchapp.core.model.PineconeUpsert
import com.example.searchapp.core.network.dto.QueryDto
import com.example.searchapp.core.network.dto.UpsertDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PineconeApi {
    @POST("/vectors/upsert")
    suspend fun upsert(@Body data: PineconeUpsert): UpsertDto

    @POST("/query")
    suspend fun query(@Body data: PineconeQuery): QueryDto
}