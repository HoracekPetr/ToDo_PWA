package com.example.plugins

import com.example.data.services.user.UserService
import com.example.routes.createUser
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()

    routing {

        //AUTH ROUTES
        createUser(userService)

        static("/static") {
            resources("static")
        }
    }
}
