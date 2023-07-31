package dev.mrz.data.model.remote.request

import dev.mrz.data.model.remote.common.RichText
import kotlinx.serialization.Serializable

@Serializable
data class CourseRequest(
    val title: String,
    val description: String,
    val tags: List<String>,
    val plannedDate: String,
    val text: List<RichText>,
)
