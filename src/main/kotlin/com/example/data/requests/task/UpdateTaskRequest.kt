package com.example.data.requests.task

data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val urgency: Int? = null
)
