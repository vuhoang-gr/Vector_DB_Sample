package com.example.searchapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpsertDto(
    val upsertedCount: Int
)
