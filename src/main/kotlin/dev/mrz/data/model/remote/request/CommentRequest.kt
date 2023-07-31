package dev.mrz.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val authorId: String,
    val text: String,
    val attachments: List<String>,
)