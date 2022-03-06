package com.example.data.repository.user

import com.example.data.models.User

interface UserRepository {

    suspend fun createUser(user: User): Boolean

    suspend fun getUserByUsername(username: String): User?

}