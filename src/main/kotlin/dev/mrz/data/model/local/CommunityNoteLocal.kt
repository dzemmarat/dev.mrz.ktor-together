package dev.mrz.data.model.local

import dev.mrz.data.model.remote.common.RichText
import dev.mrz.data.model.remote.response.CommentResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class CommunityNoteLocal(
    @BsonId
    @Contextual
    val _id: Id<CommunityNoteLocal> = newId(),
    @Contextual
    val authorId: Id<@Contextual UserLocal>,
    val title: String,
    val content: List<RichText>,
    @Contextual
    val date: Date,
    val comments: List<CommentResponse>,
) {

    val textContent: List<String?> = content.map(RichText::text) // Fixme: Doesn't need in database
}
