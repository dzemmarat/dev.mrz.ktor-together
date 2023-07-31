package dev.mrz.routes

import dev.mrz.data.dataSources.CommunityNotesDataSource
import dev.mrz.data.dataSources.CoursesDataSource
import dev.mrz.data.dataSources.StatusOfCoursesDataSource
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.data.model.remote.request.CredentialRequest
import dev.mrz.data.model.remote.response.*
import dev.mrz.isNull
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureSearchRoute(
    communityNotesDataSource: CommunityNotesDataSource,
    statusOfCoursesDataSource: StatusOfCoursesDataSource,
    coursesDataSource: CoursesDataSource,
    usersDataSource: UsersDataSource,
) {
    authenticate("auth-jwt") {
        get("/search") {
            val credentials = call.receive<CredentialRequest>()
            val user = usersDataSource.getUserByCryptCredentials(credentials.phone, credentials.passwordHashed)
            if (user.isNull()) {
                val response = ResponseWrapper(
                    data = null,
                    message = "Invalid phone or password"
                )
                call.respond(HttpStatusCode.Unauthorized, response)
                return@get
            }

            val query = call.request.queryParameters["query"] ?: run {
                val response = ResponseWrapper(
                    data = null,
                    message = "Need \"query\" parameter"
                )
                call.respond(HttpStatusCode.NotAcceptable, response)
                return@get
            }

            val communityNotesByQuery = communityNotesDataSource.getCommunityNoteByQuery(query)
            val coursesByQuery = coursesDataSource.getCoursesByQuery(query)
            val communityNotes = communityNotesByQuery.map {
                val userById = usersDataSource.getUserById(it.authorId.toString()) ?: error("Can't find user")
                val coursesOfAuthor = statusOfCoursesDataSource.getAllCoursesByUserId(userById._id.toString())
                    .map { coursesDataSource.getCourseById(it.courseId.toString()) to it.status }
                CommunityNoteResponse(
                    id = it._id.toString(),
                    title = it.title,
                    content = it.content,
                    author = UserResponse(
                        id = it.authorId.toString(),
                        name = userById.name,
                        surname = userById.surname,
                        avatar = userById.avatar,
                        role = userById.role,
                        phone = userById.phone,
                        passwordHashed = userById.passwordHashed,
                        cources = coursesOfAuthor.mapNotNull { (course, status) ->
                            course?.let {
                                CourseResponse(
                                    id = course._id.toString(),
                                    title = course.title,
                                    description = course.description,
                                    tags = course.tags,
                                    status = status.toString().lowercase(),
                                    plannedDate = course.plannedDate,
                                    text = course.text
                                )
                            }
                        }
                    ),
                    date = it.date.time.toString(),
                    comments = it.comments
                )
            }
            val courses = coursesByQuery.map {
                CourseResponse(
                    id = it._id.toString(),
                    title = it.title,
                    description = it.description,
                    tags = it.tags,
                    status = statusOfCoursesDataSource.getCourseStatusByUserId(
                        courseId = it._id.toString(),
                        userId = user._id.toString()
                    ).toString(),
                    plannedDate = it.plannedDate,
                    text = it.text
                )
            }
            val response = ResponseWrapper(
                data = SearchResponse(
                    communityNotes = communityNotes,
                    courses = courses,
                )
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }
}