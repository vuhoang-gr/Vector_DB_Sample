package com.example.searchapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PineconeUpsert(
    val vectors: List<Vector>
)
