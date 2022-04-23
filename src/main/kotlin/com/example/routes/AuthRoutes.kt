package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.requests.user.CreateUserRequest
import com.example.data.requests.user.LoginUserRequest
import com.example.data.responses.BasicApiResponse
import com.example.data.services.user.UserService
import com.example.data.util.validation.user.CreateUserValidation
import com.example.data.responses.ResponseMessages
import com.example.data.util.validation.user.LoginUserValidation
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.createUser(
    userService: UserService
){
    post(path = "/todo/user/create") {
        val createUserRequest = call.receiveOrNull<CreateUserRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        when(userService.createUser(createUserRequest)){
            is CreateUserValidation.EmptyFieldError -> {
                call.respond(
                    HttpStatusCode.Forbidden,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.FIELDS_BLANK.message)
                )
                return@post
            }
            is CreateUserValidation.UserExistsError -> {
                call.respond(
                    HttpStatusCode.Forbidden,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.USER_EXISTS.message)
                )
                return@post
            }
            is CreateUserValidation.Success -> {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(successful = true)
                )
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
){
    post(path = "/todo/user/login"){
        val loginUserRequest = call.receiveOrNull<LoginUserRequest>() ?: kotlin.run{
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        when(val login = userService.loginUser(loginUserRequest)){
            is LoginUserValidation.EmptyFieldError -> {
                call.respond(
                    HttpStatusCode.Forbidden,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.FIELDS_BLANK.message)
                )
                return@post
            }
            is LoginUserValidation.InvalidCredentialsError -> {
                call.respond(
                    HttpStatusCode.Forbidden,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.INVALID_CREDENTIALS.message)
                )
                return@post
            }
            is LoginUserValidation.Success -> {

                val userId = login.userId
                val expiresIn = 1000L * 60L * 60L * 24L * 5L

                val token = JWT.create()
                    .withClaim("userId", userId)
                    .withIssuer(jwtIssuer)
                    .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                    .withAudience(jwtAudience)
                    .sign(Algorithm.HMAC256(jwtSecret))

                call.respond(
                    HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = token
                    )
                )
            }
        }
    }
}