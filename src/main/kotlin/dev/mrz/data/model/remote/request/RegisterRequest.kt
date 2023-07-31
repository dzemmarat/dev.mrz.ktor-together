package dev.mrz.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val avatar: String?,
    val phone: String,
    val passwordHashed: String,
)
