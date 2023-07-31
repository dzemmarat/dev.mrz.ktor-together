package dev.mrz.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: String,
    val author: UserResponse,
    val text: String,
    val attachments: List<String>,
)