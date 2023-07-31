package dev.mrz.plugins

import dev.mrz.data.dataSources.CommunityNotesDataSource
import dev.mrz.data.dataSources.CoursesDataSource
import dev.mrz.data.dataSources.StatusOfCoursesDataSource
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.routes.configureAuthRoute
import dev.mrz.routes.configureCommunityNotesRoute
import dev.mrz.routes.configureCoursesRoute
import dev.mrz.routes.configureSearchRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val statusOfCoursesDataSource: StatusOfCoursesDataSource by inject()
    val coursesDataSource: CoursesDataSource by inject()
    val usersDataSource: UsersDataSource by inject()
    val communityNotesDataSource: CommunityNotesDataSource by inject()

    routing {
        configureCoursesRoute(
            statusOfCoursesDataSource = statusOfCoursesDataSource,
            coursesDataSource = coursesDataSource,
            usersDataSource = usersDataSource
        )
        configureCommunityNotesRoute(
            usersDataSource = usersDataSource,
            communityNotesDataSource = communityNotesDataSource,
            statusOfCoursesDataSource = statusOfCoursesDataSource,
            coursesDataSource = coursesDataSource
        )
        configureSearchRoute(
            communityNotesDataSource = communityNotesDataSource,
            statusOfCoursesDataSource = statusOfCoursesDataSource,
            coursesDataSource = coursesDataSource,
            usersDataSource = usersDataSource
        )
        configureAuthRoute(usersDataSource = usersDataSource)
    }
}
