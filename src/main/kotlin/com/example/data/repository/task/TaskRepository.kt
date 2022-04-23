package com.example.data.repository.task

import com.example.data.models.Task
import com.example.data.requests.task.UpdateTaskRequest

interface TaskRepository {

    suspend fun createTask(task: Task): Boolean

    suspend fun getAllTasks(userId: String, page: Int, pageSize: Int): List<Task>

    suspend fun getCompletedTasks(userId: String, page: Int, pageSize: Int): List<Task>

    suspend fun getUncompletedTasks(userId: String, page: Int, pageSize: Int): List<Task>

    suspend fun getTaskById(taskId: String): Task?

    suspend fun changeTaskCompleteStatus(taskId: String): Boolean

    suspend fun updateTask(taskId: String, updateTaskRequest: UpdateTaskRequest): Boolean

    suspend fun deleteTask(taskId: String): Boolean

}