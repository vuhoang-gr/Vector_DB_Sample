package com.example.searchapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    val score: Double,
    val values: List<Float>,
    val metadata: Metadata? = null
)