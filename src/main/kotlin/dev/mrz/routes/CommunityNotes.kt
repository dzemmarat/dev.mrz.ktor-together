package dev.mrz.routes

import dev.mrz.data.dataSources.CommunityNotesDataSource
import dev.mrz.data.dataSources.CoursesDataSource
import dev.mrz.data.dataSources.StatusOfCoursesDataSource
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.data.model.local.CommunityNoteLocal
import dev.mrz.data.model.remote.request.CommunityNoteRequest
import dev.mrz.data.model.remote.response.CommunityNoteResponse
import dev.mrz.data.model.remote.response.CourseResponse
import dev.mrz.data.model.remote.response.ResponseWrapper
import dev.mrz.data.model.remote.response.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.configureCommunityNotesRoute(
    usersDataSource: UsersDataSource,
    communityNotesDataSource: CommunityNotesDataSource,
    statusOfCoursesDataSource: StatusOfCoursesDataSource,
    coursesDataSource: CoursesDataSource,
) {
    authenticate("auth-jwt") {
        post("/community_notes") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()

            val communityNote = call.receive<CommunityNoteRequest>()
            val author = usersDataSource
                .getUserById(userId)
                ?: error("User not found")
            val communityNoteLocal = CommunityNoteLocal(
                authorId = author._id,
                title = communityNote.title,
                content = communityNote.content,
                date = Date(),
                comments = emptyList(),
            )
            communityNotesDataSource.insertCommunityNote(communityNoteLocal)

            val communityNoteResponse = CommunityNoteResponse(
                id = communityNoteLocal._id.toString(),
                title = communityNoteLocal.title,
                content = communityNoteLocal.content,
                author = UserResponse(
                    id = author._id.toString(),
                    name = author.name,
                    surname = author.surname,
                    avatar = author.avatar,
                    role = author.role,
                    phone = author.phone,
                    passwordHashed = author.passwordHashed,
                    cources = listOf(),
                ),
                date = communityNoteLocal.date.time.toString(),
                comments = communityNoteLocal.comments
            )
            val response = ResponseWrapper(
                data = communityNoteResponse,
            )
            call.respond(HttpStatusCode.OK, response)
        }

        get("/community_notes") {
            val communityNoteResponse = communityNotesDataSource.getAllCommunityNotes().map {
                val authorLocal = usersDataSource.getUserById(it.authorId.toString()) ?: error("User not found")
                val coursesOfAuthor = statusOfCoursesDataSource.getAllCoursesByUserId(authorLocal._id.toString())
                    .map { coursesDataSource.getCourseById(it.courseId.toString()) to it.status }
                val author = UserResponse(
                    id = authorLocal._id.toString(),
                    name = authorLocal.name,
                    surname = authorLocal.surname,
                    avatar = authorLocal.avatar,
                    role = authorLocal.role,
                    phone = authorLocal.phone,
                    passwordHashed = authorLocal.passwordHashed,
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
                )
                CommunityNoteResponse(
                    id = it._id.toString(),
                    title = it.title,
                    content = it.content,
                    author = author,
                    date = it.date.time.toString(),
                    comments = it.comments
                )
            }
            val response = ResponseWrapper(data = communityNoteResponse)
            call.respond(HttpStatusCode.OK, response)
        }

        get("/community_notes/{id}") {
            val id = call.parameters["id"] ?: run {
                val response = ResponseWrapper(
                    message = "Need an \"id\" parameter",
                    data = null
                )
                call.respond(HttpStatusCode.BadRequest, response)
                return@get
            }

            val communityNote = communityNotesDataSource.getCommunityNoteById(id) ?: run {
                val response = ResponseWrapper(
                    message = "Can't find community note with id: $id",
                    data = null
                )
                call.respond(HttpStatusCode.NotFound, response)
                return@get
            }
            val authorLocal = usersDataSource.getUserById(communityNote.authorId.toString()) ?: run {
                val response = ResponseWrapper(
                    message = "Can't find author of community note. " +
                            "Author id: ${communityNote.authorId}. Community note: $id",
                    data = null
                )
                call.respond(HttpStatusCode.NotFound, response)
                return@get
            }

            val coursesOfAuthor = statusOfCoursesDataSource.getAllCoursesByUserId(authorLocal._id.toString())
                .map { coursesDataSource.getCourseById(it.courseId.toString()) to it.status }
            val author = UserResponse(
                id = authorLocal._id.toString(),
                name = authorLocal.name,
                surname = authorLocal.surname,
                avatar = authorLocal.avatar,
                role = authorLocal.role,
                phone = authorLocal.phone,
                passwordHashed = authorLocal.passwordHashed,
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
            )

            val communityNoteResponse = CommunityNoteResponse(
                id = communityNote._id.toString(),
                title = communityNote.title,
                content = communityNote.content,
                author = author,
                date = communityNote.date.time.toString(),
                comments = communityNote.comments
            )
            val response = ResponseWrapper(data = communityNoteResponse)
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
