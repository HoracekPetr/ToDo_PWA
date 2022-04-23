package com.example.data.services.user

import com.example.data.models.User
import com.example.data.repository.user.UserRepository
import com.example.data.requests.user.CreateUserRequest
import com.example.data.requests.user.LoginUserRequest
import com.example.data.util.validation.user.CreateUserValidation
import com.example.data.util.validation.user.LoginUserValidation
import org.mindrot.jbcrypt.BCrypt

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun createUser(createUserRequest: CreateUserRequest): CreateUserValidation {
        if(createUserRequest.username.isBlank() || createUserRequest.password.isBlank()){
            return CreateUserValidation.EmptyFieldError
        }

        if(doesUserAlreadyExist(createUserRequest.username)){
            return CreateUserValidation.UserExistsError
        }

        return CreateUserValidation.Success(request = userRepository.createUser(user = User(
            username = createUserRequest.username,
            password = BCrypt.hashpw(createUserRequest.password, BCrypt.gensalt())
        )))
    }

    suspend fun loginUser(loginUserRequest: LoginUserRequest): LoginUserValidation {

        if(loginUserRequest.username.isBlank() || loginUserRequest.password.isBlank()){
            return LoginUserValidation.EmptyFieldError
        }

        if(!doesPasswordMatchForUser(loginUserRequest.username, loginUserRequest.password)){
            return LoginUserValidation.InvalidCredentialsError
        }

        return LoginUserValidation.Success(userId = userRepository.getUserByUsername(loginUserRequest.username)?.id)

    }


    private suspend fun doesUserAlreadyExist(username: String): Boolean {
        return userRepository.getUserByUsername(username = username) != null
    }

    private suspend fun doesPasswordMatchForUser(username: String, inputPassword: String): Boolean {
        val user = userRepository.getUserByUsername(username = username) ?: return false
        return BCrypt.checkpw(inputPassword, user.password)
    }

}