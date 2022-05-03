package com.example.data.services.task

import com.example.data.models.Task
import com.example.data.repository.task.TaskRepository
import com.example.data.requests.task.CreateTaskRequest
import com.example.data.requests.task.UpdateTaskRequest
import com.example.data.util.validation.ValidationConstants.TITLE_CHAR_LIMIT
import com.example.data.util.task.Urgency
import com.example.data.util.validation.task.StandardTaskValidation

class TaskService(
    private val taskRepository: TaskRepository
) {

    suspend fun createTask(createTaskRequest: CreateTaskRequest, ownerId: String): StandardTaskValidation {

        if(createTaskRequest.title.isBlank()){
            return StandardTaskValidation.EmptyTitleError
        }

        if(createTaskRequest.title.length > TITLE_CHAR_LIMIT){
            return StandardTaskValidation.TitleTooLongError
        }

        if(createTaskRequest.urgency !in (Urgency.Low.urgency..Urgency.High.urgency)){
            return StandardTaskValidation.InvalidUrgencyValueError
        }

        return StandardTaskValidation.Success(
            request = taskRepository.createTask(
                Task(
                    ownerId = ownerId,
                    title = createTaskRequest.title,
                    description = createTaskRequest.description,
                    urgency = createTaskRequest.urgency
                )
            )
        )
    }

    suspend fun updateTask(taskId: String, updateTaskRequest: UpdateTaskRequest): StandardTaskValidation{

        updateTaskRequest.title?.let { newTitle ->
            if(newTitle.isBlank()){
                return StandardTaskValidation.EmptyTitleError
            }

            if(newTitle.length > TITLE_CHAR_LIMIT){
                return StandardTaskValidation.TitleTooLongError
            }
        }

        updateTaskRequest.urgency?.let { newUrgency ->
            if(newUrgency !in (Urgency.Low.urgency..Urgency.High.urgency)){
                return StandardTaskValidation.InvalidUrgencyValueError
            }
        }

        return StandardTaskValidation.Success(taskRepository.updateTask(taskId, updateTaskRequest))
    }

    suspend fun getTaskById(taskId: String) = taskRepository.getTaskById(taskId)

    suspend fun getAllTasks(userId: String, page: Int, pageSize: Int) = taskRepository.getAllTasks(userId, page, pageSize)

    suspend fun getCompletedTasks(userId: String, page: Int, pageSize: Int) = taskRepository.getCompletedTasks(userId, page, pageSize)

    suspend fun getUncompletedTasks(userId: String, page: Int, pageSize: Int) = taskRepository.getUncompletedTasks(userId, page, pageSize)

    suspend fun changeTaskCompleteStatus(taskId: String) = taskRepository.changeTaskCompleteStatus(taskId)

    suspend fun deleteTask(taskId: String) = taskRepository.deleteTask(taskId)

    suspend fun searchTasks(searchQuery: String, page: Int, pageSize: Int, userId: String) = taskRepository.searchTasks(searchQuery, page, pageSize, userId)

}