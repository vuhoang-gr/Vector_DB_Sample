package com.example.searchapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PineconeQuery(
    val namespace: String ,
    val vector: List<Float>,
    val topK: Int,
    val includeValues: Boolean,
    val includeMetadata: Boolean
)
