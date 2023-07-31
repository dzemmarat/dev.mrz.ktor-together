package dev.mrz.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<out T>(
    val status: Int = 0,
    val message: String? = null,
    val data: T?
)
