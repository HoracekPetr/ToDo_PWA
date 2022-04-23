package com.example.di

import com.example.data.repository.task.TaskRepository
import com.example.data.repository.task.TaskRepositoryImpl
import com.example.data.repository.user.UserRepository
import com.example.data.repository.user.UserRepositoryImpl
import com.example.data.services.task.TaskService
import com.example.data.services.user.UserService
import com.example.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient(System.getenv("MONGO_URI")).coroutine
        client.getDatabase(DATABASE_NAME)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single {
        UserService(get())
    }

    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }

    single {
        TaskService(get())
    }
}