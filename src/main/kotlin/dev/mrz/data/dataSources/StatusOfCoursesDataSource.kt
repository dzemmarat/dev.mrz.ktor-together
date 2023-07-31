package dev.mrz.data.dataSources

import dev.mrz.data.model.local.CourseStatus
import dev.mrz.data.model.local.StatusOfCourseLocal

interface StatusOfCoursesDataSource {
    suspend fun getAllCoursesByUserId(userId: String): List<StatusOfCourseLocal>
    suspend fun insertCourseForUserId(courseId: String, userId: String)
    suspend fun changeStatusOfCourse(courseId: String, userId: String, status: CourseStatus)
    suspend fun getCourseStatusByUserId(courseId: String, userId: String): CourseStatus?
}