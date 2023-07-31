package dev.mrz.data.model.local

import dev.mrz.data.model.remote.common.RichText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNull.content
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CourseLocal(
    @BsonId
    @Contextual
    val _id: Id<CourseLocal> = newId(),
    val title: String,
    val description: String,
    val plannedDate: String,
    val text: List<RichText>,
    val tags: List<String>,
) {
    val textContent = text.map(RichText::text)
}
