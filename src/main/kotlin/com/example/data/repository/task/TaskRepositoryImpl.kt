package com.example.data.repository.task

import com.example.data.models.Task
import com.example.data.requests.task.UpdateTaskRequest
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.util.function.Predicate.not

class TaskRepositoryImpl(
    db: CoroutineDatabase
) : TaskRepository {

    private val tasks = db.getCollection<Task>()

    override suspend fun createTask(task: Task): Boolean {
        return tasks.insertOne(task).wasAcknowledged()
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return tasks.findOneById(id = taskId)
    }

    override suspend fun getAllTasks(userId: String, page: Int, pageSize: Int): List<Task> {
        return tasks
            .find(Task::ownerId eq userId)
            .skip(page * pageSize)
            .limit(page)
            .descendingSort(Task::urgency)
            .toList()
    }

    override suspend fun getCompletedTasks(userId: String, page: Int, pageSize: Int): List<Task> {
        return tasks
            .find(and(Task::ownerId eq userId, Task::completed eq true))
            .skip(page * pageSize)
            .limit(page)
            .descendingSort(Task::urgency)
            .toList()
    }

    override suspend fun getUncompletedTasks(userId: String, page: Int, pageSize: Int): List<Task> {
        return tasks
            .find(and(Task::ownerId eq userId, Task::completed eq false))
            .skip(page * pageSize)
            .limit(page)
            .descendingSort(Task::urgency)
            .toList()
    }

    override suspend fun changeTaskCompleteStatus(taskId: String): Boolean {
        val taskCompleteStatus = getTaskById(taskId)?.completed ?: return false
        return tasks.updateOneById(taskId, update = setValue(Task::completed, !taskCompleteStatus)).wasAcknowledged()
    }

    override suspend fun searchTasks(searchQuery: String, page: Int, pageSize: Int, userId: String): List<Task> {
        return tasks
            .find(and(Task::ownerId eq userId, Task::title regex Regex("(?i).*${searchQuery}.*")))
            .skip(page * pageSize)
            .limit(page)
            .descendingSort(Task::urgency)
            .toList()
    }

    override suspend fun updateTask(taskId: String, updateTaskRequest: UpdateTaskRequest): Boolean {

        val task = getTaskById(taskId) ?: return false

        return tasks.updateOneById(
            id = taskId,
            update = Task(
                id = task.id,
                ownerId = task.ownerId,
                title = updateTaskRequest.title ?: task.title,
                description = updateTaskRequest.description ?: task.description,
                urgency = updateTaskRequest.urgency ?: task.urgency,
                completed = task.completed
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        return tasks.deleteOneById(taskId).wasAcknowledged()
    }
}