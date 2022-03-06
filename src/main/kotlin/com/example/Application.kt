package com.example

import com.example.di.mainModule
import io.ktor.application.*
import com.example.plugins.*
import org.koin.core.Koin
import org.koin.ktor.ext.modules

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(org.koin.ktor.ext.Koin){
        modules(mainModule)
    }
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureSecurity()
}
