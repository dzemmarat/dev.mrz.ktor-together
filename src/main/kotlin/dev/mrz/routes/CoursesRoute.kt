package dev.mrz.routes

import dev.mrz.data.dataSources.CoursesDataSource
import dev.mrz.data.dataSources.StatusOfCoursesDataSource
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.data.model.local.CourseLocal
import dev.mrz.data.model.local.UserLocal
import dev.mrz.data.model.remote.request.CourseRequest
import dev.mrz.data.model.remote.response.CourseResponse
import dev.mrz.data.model.remote.response.ResponseWrapper
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureCoursesRoute(
    coursesDataSource: CoursesDataSource,
    statusOfCoursesDataSource: StatusOfCoursesDataSource,
    usersDataSource: UsersDataSource,
) {
    authenticate("auth-jwt") {
        post("/courses") {
            val course = call.receive<CourseRequest>()
            val courseLocal = CourseLocal(
                title = course.title,
                description = course.description,
                tags = course.tags,
                plannedDate = course.plannedDate,
                text = course.text
            )
            coursesDataSource.insertCourse(courseLocal)
            val userIds = usersDataSource.getAllUsers().map(UserLocal::_id)
            userIds.forEach {
                statusOfCoursesDataSource.insertCourseForUserId(
                    courseLocal._id.toString(),
                    it.toString()
                )
            }

            val response = ResponseWrapper(data = null)
            call.respond(response)
        }

        get("/courses") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()

            val coursesOfUser = statusOfCoursesDataSource.getAllCoursesByUserId(userId)
            println(coursesOfUser)
            val courses = coursesOfUser.mapNotNull { statusOfCourseLocal ->
                println("Course id: ${statusOfCourseLocal.courseId}")
                val courseInfo = coursesDataSource.getCourseById(statusOfCourseLocal.courseId.toString())
                println(courseInfo)
                courseInfo?.let {
                    CourseResponse(
                        id = courseInfo._id.toString(),
                        title = courseInfo.title,
                        description = courseInfo.description,
                        tags = courseInfo.tags,
                        status = statusOfCourseLocal.status.toString().lowercase(),
                        plannedDate = courseInfo.plannedDate,
                        text = courseInfo.text
                    )
                }
            }
            val response = ResponseWrapper(data = courses)
            call.respond(response)
        }
    }
}