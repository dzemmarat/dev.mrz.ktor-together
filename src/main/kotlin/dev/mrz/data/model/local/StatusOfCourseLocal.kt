package dev.mrz.data.model.local

import com.fasterxml.jackson.annotation.JsonFormat
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class StatusOfCourseLocal(
    @BsonId
    @Contextual
    val _id: Id<StatusOfCourseLocal> = newId(),
    @Contextual
    val userId: Id<@Contextual UserLocal>,
    @Contextual
    val courseId: Id<CourseLocal>,
    val status: CourseStatus = CourseStatus.NEW,
)

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class CourseStatus {
    NEW,
    CHECKED,
    SENT_FOR_REVIEW,
    REVIEW_IN_PROGRESS,
    REJECTED,
    COMPLETED
}
