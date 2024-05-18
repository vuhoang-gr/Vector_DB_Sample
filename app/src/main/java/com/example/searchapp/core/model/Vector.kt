package com.example.searchapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Vector(
    val id: String,
    val values: List<Float>,
    val metadata: Map<String, String>
)

