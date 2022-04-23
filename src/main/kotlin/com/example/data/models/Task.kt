package com.example.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Task(
    @BsonId
    val id: String = ObjectId().toString(),
    val ownerId: String,
    val title: String,
    val description: String?,
    val urgency: Int,
    val completed: Boolean = false
)
