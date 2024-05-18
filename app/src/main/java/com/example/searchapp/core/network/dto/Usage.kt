package com.example.searchapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    val readUnits: Int
)