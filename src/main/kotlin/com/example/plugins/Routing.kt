package com.example.plugins

import com.example.data.services.task.TaskService
import com.example.data.services.user.UserService
import com.example.routes.*
import io.ktor.routing.*
import io.ktor.http.content.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()
    val taskService: TaskService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {

        //AUTH ROUTES
        authenticate()
        createUser(userService = userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        //TASK ROUTES
        createTask(taskService)
        getTaskById(taskService)
        getAllTasks(taskService)
        getCompletedTasks(taskService)
        getUncompletedTasks(taskService)
        searchTasks(taskService)
        changeTaskCompleteStatus(taskService)
        updateTask(taskService)
        deleteTask(taskService)

        static("/static") {
            resources("static")
        }
    }
}
