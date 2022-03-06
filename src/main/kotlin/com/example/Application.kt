package com.example

import io.ktor.application.*
import com.example.plugins.*
import org.koin.core.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    install(org.koin.ktor.ext.Koin){

    }
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureSecurity()
}
