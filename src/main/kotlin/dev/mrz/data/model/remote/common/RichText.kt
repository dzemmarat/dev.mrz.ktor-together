package dev.mrz.data.model.remote.common

import kotlinx.serialization.Serializable

@Serializable
data class RichText(
    val text: String? = null,
    val image: String? = null,
)