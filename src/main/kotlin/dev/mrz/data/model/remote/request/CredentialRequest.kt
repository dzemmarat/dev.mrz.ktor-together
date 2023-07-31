package dev.mrz.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CredentialRequest(
    val phone: String,
    val passwordHashed: String,
)
