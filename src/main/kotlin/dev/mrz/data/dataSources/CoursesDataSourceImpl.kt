package dev.mrz.data.dataSources

import dev.mrz.data.model.local.CourseLocal
import org.bson.types.ObjectId
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.or

class CoursesDataSourceImpl(
    database: CoroutineDatabase,
) : CoursesDataSource {

    private val courseCollection = database.getCollection<CourseLocal>()

    override suspend fun getCourseById(id: String): CourseLocal? =
        courseCollection.findOneById(ObjectId(id))

    override suspend fun getCoursesByQuery(query: String): List<CourseLocal> =
        courseCollection.find(or(CourseLocal::textContent contains query)).toList()

    override suspend fun getAllCourses(): List<CourseLocal> = courseCollection.find().toList()
    override suspend fun insertCourse(course: CourseLocal) = courseCollection.insertOne(course)
    override suspend fun updateCourse(course: CourseLocal) = courseCollection.updateOneById(course._id, course)
    override suspend fun deleteCourse(id: String) = courseCollection.deleteOneById(id)
}