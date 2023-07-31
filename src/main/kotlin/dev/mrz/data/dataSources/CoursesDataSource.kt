package dev.mrz.data.dataSources

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import dev.mrz.data.model.local.CourseLocal

interface CoursesDataSource {
    suspend fun getAllCourses(): List<CourseLocal>
    suspend fun getCourseById(id: String): CourseLocal?
    suspend fun getCoursesByQuery(query: String): List<CourseLocal>
    suspend fun insertCourse(course: CourseLocal): InsertOneResult
    suspend fun updateCourse(course: CourseLocal): UpdateResult
    suspend fun deleteCourse(id: String): DeleteResult
}