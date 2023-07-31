package dev.mrz.data.model.remote.request

import dev.mrz.data.model.remote.common.RichText
import kotlinx.serialization.Serializable

@Serializable
data class CommunityNoteRequest(
    val title: String,
    val content: List<RichText>,
)
