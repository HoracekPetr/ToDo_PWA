package com.example.di

import com.example.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient(System.getenv("MONGO_URI")).coroutine
        client.getDatabase(DATABASE_NAME)
    }
}