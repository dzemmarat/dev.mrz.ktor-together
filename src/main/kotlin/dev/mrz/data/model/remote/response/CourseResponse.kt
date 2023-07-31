package dev.mrz.data.model.remote.response

import dev.mrz.data.model.remote.common.RichText
import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    val id: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val status: String,
    val plannedDate: String,
    val text: List<RichText>,
)
