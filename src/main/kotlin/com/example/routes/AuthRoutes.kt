package com.example.routes

import com.example.data.requests.user.CreateUserRequest
import com.example.data.requests.user.LoginUserRequest
import com.example.data.responses.BasicApiResponse
import com.example.data.services.user.UserService
import com.example.util.validation.CreateUserValidation
import com.example.util.ResponseMessages
import com.example.util.validation.LoginUserValidation
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUser(
    userService: UserService
){
    post(path = "/todo/user/create") {
        val createUserRequest = call.receiveOrNull<CreateUserRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        when(userService.validateUserCreation(createUserRequest)){
            CreateUserValidation.EmptyFieldError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.FIELDS_BLANK.message))
                return@post
            }
            CreateUserValidation.UserExistsError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.USER_EXISTS.message))
                return@post
            }
            CreateUserValidation.Success -> {
                userService.createUser(createUserRequest)
                call.respond(HttpStatusCode.OK, BasicApiResponse<Unit>(successful = true))
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService
){
    post(path = "/todo/user/login"){
        val loginUserRequest = call.receiveOrNull<LoginUserRequest>() ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        when(userService.validateUserLogin(loginUserRequest)){
            LoginUserValidation.EmptyFieldError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.FIELDS_BLANK.message))
                return@post
            }
            LoginUserValidation.InvalidCredentialsError -> {
                call.respond(message = BasicApiResponse<Unit>(successful = false, message = ResponseMessages.INVALID_CREDENTIALS.message))
                return@post
            }
            LoginUserValidation.Success -> {
                call.respond(
                    HttpStatusCode.OK,
                    message = BasicApiResponse<Unit>(successful = true)
                )
            }
        }
    }
}