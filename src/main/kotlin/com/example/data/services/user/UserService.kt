package com.example.data.services.user

import com.example.data.models.User
import com.example.data.repository.user.UserRepository
import com.example.data.requests.user.CreateUserRequest
import com.example.data.requests.user.LoginUserRequest
import com.example.util.validation.CreateUserValidation
import com.example.util.validation.LoginUserValidation

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun createUser(createUserRequest: CreateUserRequest): Boolean = userRepository.createUser(
        User(username = createUserRequest.username, password = createUserRequest.password)
    )

    private suspend fun getUserByUsername(username: String): User? = userRepository.getUserByUsername(username)

    private suspend fun doesUserAlreadyExist(username: String): Boolean {
        return getUserByUsername(username = username) != null
    }

    private suspend fun doesPasswordMatchForUser(username: String, inputPassword: String): Boolean {
        val user = getUserByUsername(username = username) ?: return false
        return user.password == inputPassword
    }


    suspend fun validateUserCreation(createUserRequest: CreateUserRequest): CreateUserValidation {
        return when{
            createUserRequest.username.isBlank() || createUserRequest.password.isBlank() -> CreateUserValidation.EmptyFieldError
            doesUserAlreadyExist(createUserRequest.username) -> CreateUserValidation.UserExistsError
            else -> CreateUserValidation.Success
        }
    }

    suspend fun validateUserLogin(loginUserRequest: LoginUserRequest): LoginUserValidation{
        return when{
            loginUserRequest.username.isBlank() || loginUserRequest.password.isBlank() -> LoginUserValidation.EmptyFieldError
            !doesPasswordMatchForUser(loginUserRequest.username, loginUserRequest.password) -> LoginUserValidation.InvalidCredentialsError
            else -> LoginUserValidation.Success
        }
    }
}