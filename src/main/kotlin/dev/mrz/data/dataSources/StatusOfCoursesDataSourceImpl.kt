package dev.mrz.data.dataSources

import dev.mrz.data.model.local.CourseStatus
import dev.mrz.data.model.local.StatusOfCourseLocal
import org.bson.types.ObjectId
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId

class StatusOfCoursesDataSourceImpl(
    database: CoroutineDatabase,
) : StatusOfCoursesDataSource {

    private val statusOfCoursesCollection = database.getCollection<StatusOfCourseLocal>()

    override suspend fun getAllCoursesByUserId(userId: String): List<StatusOfCourseLocal> =
        statusOfCoursesCollection
            .find(StatusOfCourseLocal::userId eq ObjectId(userId).toId())
            .toList()

    override suspend fun insertCourseForUserId(courseId: String, userId: String) {
        statusOfCoursesCollection.insertOne(
            StatusOfCourseLocal(
                courseId = ObjectId(courseId).toId(),
                userId = ObjectId(userId).toId()
            )
        )
    }

    override suspend fun changeStatusOfCourse(courseId: String, userId: String, status: CourseStatus) {
        statusOfCoursesCollection.updateOne(
            filter = and(
                (StatusOfCourseLocal::courseId eq ObjectId(courseId).toId()),
                (StatusOfCourseLocal::userId eq ObjectId(userId).toId())
            ),
            update = StatusOfCourseLocal::status eq status
        )
    }

    override suspend fun getCourseStatusByUserId(courseId: String, userId: String): CourseStatus? =
        statusOfCoursesCollection.findOne(
            and(
                StatusOfCourseLocal::userId eq ObjectId(userId).toId(),
                StatusOfCourseLocal::courseId eq ObjectId(courseId).toId()
            )
        )?.status
}