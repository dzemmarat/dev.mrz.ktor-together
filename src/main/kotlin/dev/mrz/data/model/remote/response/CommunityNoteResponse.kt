package dev.mrz.data.model.remote.response

import dev.mrz.data.model.remote.common.RichText
import kotlinx.serialization.Serializable

@Serializable
data class CommunityNoteResponse(
    val id: String,
    val title: String,
    val content: List<RichText>,
    val author: UserResponse,
    val date: String,
    val comments: List<CommentResponse>,
)
