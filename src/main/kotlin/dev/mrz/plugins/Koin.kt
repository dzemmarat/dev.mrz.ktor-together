package dev.mrz.plugins

import io.ktor.server.application.*
import dev.mrz.di.mainModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(mainModule)
    }
}