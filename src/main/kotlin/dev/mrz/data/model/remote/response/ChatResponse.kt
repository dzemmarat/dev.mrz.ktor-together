package dev.mrz.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    val name: String,
    val image: String?,
    val messages: List<MessageResponse>,
    val users: List<UserResponse>,
)
