package dev.mrz.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.data.model.local.UserLocal
import dev.mrz.data.model.remote.request.CredentialRequest
import dev.mrz.data.model.remote.request.RegisterRequest
import dev.mrz.data.model.remote.response.ResponseWrapper
import dev.mrz.data.model.remote.response.TokenResponse
import dev.mrz.isNull
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.configureAuthRoute(
    usersDataSource: UsersDataSource,
) {
    post("/auth") {
        val credentials = call.receive<CredentialRequest>()

        val user = usersDataSource.getUserByCryptCredentials(credentials.phone, credentials.passwordHashed)
        if (user.isNull()) {
            val response = ResponseWrapper(
                data = null,
                message = "Invalid phone or password"
            )
            call.respond(HttpStatusCode.Unauthorized, response)
            return@post
        }

        val token = JWT.create()
            .withAudience("aud")
            .withIssuer("issuer")
            .withClaim("id", user._id.toString())
            .withClaim("phone", credentials.phone)
            .withClaim("passwordMD", user.passwordHashed)
            .withExpiresAt(Date(System.currentTimeMillis() + 60_000_000L))
            .sign(Algorithm.HMAC256("Hmac"))

        val response = ResponseWrapper(
            data = TokenResponse(
                token = token,
            ),
        )
        call.respond(HttpStatusCode.OK, response)
    }

    post("/register") {
        val credentials = call.receive<RegisterRequest>()

        val isUserExist = usersDataSource.isUserExist(credentials.phone)
        if (isUserExist) {
            val response = ResponseWrapper(
                data = null,
                message = "User exist"
            )
            call.respond(HttpStatusCode.Conflict, response)
            return@post
        }

        val user = UserLocal(
            phone = credentials.phone,
            passwordHashed = credentials.passwordHashed,
            name = credentials.name,
            surname = credentials.surname,
            avatar = credentials.avatar,
        )
        /*val insertResult = */usersDataSource.insertUser(user)
        // Fixme: check if id is not null. Now doesn't work
//        if (insertResult.insertedId.isNull) {
//            val response = ResponseWrapper(
//                data = null,
//                message = "Can't create user"
//            )
//            call.respond(HttpStatusCode.Unauthorized)
//            return@post
//        }
        val token = JWT.create()
            .withAudience("aud")
            .withIssuer("issuer")
            .withClaim("id", user._id.toString())
            .withClaim("phone", credentials.phone)
            .withClaim("passwordMD", user.passwordHashed)
            .withExpiresAt(Date(System.currentTimeMillis() + 60_000_000L))
            .sign(Algorithm.HMAC256("Hmac"))

        val response = ResponseWrapper(data = TokenResponse(token = token))
        call.respond(response)
    }
}
