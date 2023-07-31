package dev.mrz.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val surname: String,
    val avatar: String?,
    val role: String,
    val phone: String,
    val passwordHashed: String,
    val cources: List<CourseResponse>
)
