package dev.mrz.data.model.local

import dev.mrz.data.model.remote.response.UserResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

@Serializable
data class CommentLocal(
    @BsonId
    @Contextual
    val _id: Id<CommentLocal>,
    @Contextual
    val author: Id<UserResponse>,
    val text: String,
    val attachments: List<String>,
)
