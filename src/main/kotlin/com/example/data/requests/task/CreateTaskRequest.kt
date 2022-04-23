package com.example.data.requests.task

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val urgency: Int
)