package dev.mrz.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.mrz.data.dataSources.UsersDataSource
import dev.mrz.isNotNull
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userDataSource by inject<UsersDataSource>()

    val secret = "Hmac"
    val issuer = "issuer"
    val audience = "aud"
    val myRealm = "realm"

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                val payload = credential.payload
                val login = payload.getClaim("phone").asString()
                val password = payload.getClaim("passwordMD").asString()

                if (login.isEmpty() || password.isEmpty()) return@validate null

                if (userDataSource.getUserByCryptCredentials(login, password).isNotNull()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
