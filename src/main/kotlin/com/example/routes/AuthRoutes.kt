package com.example.routes

import com.example.data.repository.user.UserRepository
import com.example.data.requests.user.CreateUserRequest
import com.example.data.responses.BasicApiResponse
import com.example.data.services.user.UserService
import com.example.util.CreateUserValidation
import com.example.util.ResponseMessages
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUser(
    userService: UserService
){
    post(path = "/todo/user/create") {
        val userRequest = call.receiveOrNull<CreateUserRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        when(userService.validateUserCreation(userRequest)){
            CreateUserValidation.EmptyFieldError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.FIELDS_BLANK.message))
                return@post
            }
            CreateUserValidation.UserExistsError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.USER_EXISTS.message))
                return@post
            }
            CreateUserValidation.Success -> {
                userService.createUser(userRequest)
                call.respond(HttpStatusCode.OK, BasicApiResponse<Unit>(successful = true))
            }
        }
    }
}