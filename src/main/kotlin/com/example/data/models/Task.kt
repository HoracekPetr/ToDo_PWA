package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Task(
    @BsonId
    val id: String = ObjectId().toString(),
    val title: String,
    val description: String,
    val members: List<String>,
    val completed: Boolean
)
