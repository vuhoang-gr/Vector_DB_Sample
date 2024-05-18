package com.example.searchapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class QueryDto(
    val matches: List<Match>,
    val namespace: String,
    val results: List<String>,
    val usage: Usage
)