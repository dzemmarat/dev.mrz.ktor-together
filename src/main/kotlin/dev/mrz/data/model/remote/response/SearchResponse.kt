package dev.mrz.data.model.remote.response

data class SearchResponse(
    val communityNotes: List<CommunityNoteResponse>,
    val courses: List<CourseResponse>,
)
