package dev.mrz.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
)
