package com.example.routes

import com.example.data.requests.task.CreateTaskRequest
import com.example.data.requests.task.UpdateTaskRequest
import com.example.data.responses.BasicApiResponse
import com.example.data.services.task.TaskService
import com.example.data.responses.ResponseMessages
import com.example.data.util.task.TaskConstants.DEFAULT_PAGE
import com.example.data.util.task.TaskConstants.DEFAULT_PAGE_SIZE
import com.example.data.util.task.TaskConstants.PAGE
import com.example.data.util.task.TaskConstants.PAGE_SIZE
import com.example.data.util.task.TaskConstants.TASK_ID
import com.example.data.util.validation.task.StandardTaskValidation
import com.example.plugins.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createTask(
    taskService: TaskService
){
    authenticate {
        post(path = "/todo/task/create"){
            val createTaskRequest = call.receiveOrNull<CreateTaskRequest>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest,
                    BasicApiResponse<Unit>(successful = false)
                )
                return@post
            }

            val userId = call.userId

            when(taskService.createTask(createTaskRequest = createTaskRequest, ownerId = userId)){

                is StandardTaskValidation.EmptyTitleError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(successful = false, message = ResponseMessages.TITLE_BLANK.message)
                    )
                    return@post
                }

                is StandardTaskValidation.InvalidUrgencyValueError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(successful = false, message = ResponseMessages.INVALID_URGENCY.message)
                    )
                }

                StandardTaskValidation.TitleTooLongError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(successful = false, message = ResponseMessages.TITLE_TOO_LONG.message)
                    )
                }

                is StandardTaskValidation.Success -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(successful = true)
                    )
                }
            }
        }
    }
}

fun Route.getTaskById(
    taskService: TaskService
){
    authenticate {
        get(path = "/todo/task"){
            val taskId = call.parameters[TASK_ID] ?: ""

            val userId = call.userId

            val task = taskService.getTaskById(taskId)

            if(task == null){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_NOT_FOUND.message
                    )
                )
                return@get
            }

            if(task.ownerId != userId){
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_NOT_ALLOWED.message
                    )
                )
                return@get
            }

            call.respond(
                BasicApiResponse(
                    successful = true,
                    data = task
                )
            )
        }
    }
}


fun Route.getAllTasks(
    taskService: TaskService
){
    authenticate {
        get(path = "/todo/task/all") {
            val page = call.parameters[PAGE]?.toIntOrNull() ?: DEFAULT_PAGE
            val pageSize = call.parameters[PAGE_SIZE]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
            val userId = call.userId

            val allTasks = taskService.getAllTasks(userId = userId, page = page, pageSize = pageSize)

            if(allTasks.isEmpty()){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.NO_TASKS.message)
                )
                return@get
            }

            call.respond(
                BasicApiResponse(
                    successful = true,
                    data = allTasks
                )
            )
        }
    }
}

fun Route.getCompletedTasks(
    taskService: TaskService
){
    authenticate {
        get(path = "/todo/task/all/completed") {
            val page = call.parameters[PAGE]?.toIntOrNull() ?: DEFAULT_PAGE
            val pageSize = call.parameters[PAGE_SIZE]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
            val userId = call.userId

            val completeTasks = taskService.getCompletedTasks(userId = userId, page = page, pageSize = pageSize)

            if(completeTasks.isEmpty()){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.NO_COMPLETED_TASKS.message)
                )
                return@get
            }

            call.respond(
                BasicApiResponse(
                    successful = true,
                    data = completeTasks
                )
            )
        }
    }
}

fun Route.getUncompletedTasks(
    taskService: TaskService
){
    authenticate {
        get(path = "/todo/task/all/uncompleted") {
            val page = call.parameters[PAGE]?.toIntOrNull() ?: DEFAULT_PAGE
            val pageSize = call.parameters[PAGE_SIZE]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
            val userId = call.userId

            val incompleteTasks = taskService.getUncompletedTasks(userId = userId, page = page, pageSize = pageSize)

            if(incompleteTasks.isEmpty()){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(successful = false, message = ResponseMessages.NO_UNCOMPLETED_TASKS.message)
                )
                return@get
            }

            call.respond(
                BasicApiResponse(
                    successful = true,
                    data = incompleteTasks
                )
            )
        }
    }
}

fun Route.changeTaskCompleteStatus(
    taskService: TaskService
){
    authenticate {
        put(path = "/todo/task/complete"){

            val taskId = call.parameters[TASK_ID] ?: ""

            val userId = call.userId

            val task = taskService.getTaskById(taskId)

            if(task == null){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_NOT_FOUND.message
                    )
                )
                return@put
            }

            if(task.ownerId != userId){
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_CHANGE_NOT_ALLOWED.message
                    )
                )
                return@put
            }

            val changeTaskCompleteStatus = taskService.changeTaskCompleteStatus(taskId)

            if(!changeTaskCompleteStatus){
                call.respond(
                    HttpStatusCode.BadRequest,
                    BasicApiResponse<Unit>(
                        successful = false,
                    )
                )
                return@put
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = true,
                )
            )
        }
    }
}

fun Route.updateTask(
    taskService: TaskService
) {
    authenticate {
        put(path = "/todo/task/update"){

            val taskId = call.parameters[TASK_ID] ?: ""
            val updateTaskRequest = call.receiveOrNull<UpdateTaskRequest>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@put
            }

            val userId = call.userId
            val task = taskService.getTaskById(taskId)

            if(task == null){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_NOT_FOUND.message
                    )
                )
                return@put
            }

            if(task.ownerId != userId){
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_CHANGE_NOT_ALLOWED.message
                    )
                )
                return@put
            }

            when(taskService.updateTask(taskId = taskId, updateTaskRequest = updateTaskRequest)){
                is StandardTaskValidation.EmptyTitleError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ResponseMessages.TITLE_BLANK.message
                        )
                    )
                }
                is StandardTaskValidation.InvalidUrgencyValueError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ResponseMessages.INVALID_URGENCY.message
                        )
                    )
                }

                is StandardTaskValidation.TitleTooLongError -> {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ResponseMessages.TITLE_TOO_LONG.message
                        )
                    )
                }
                is StandardTaskValidation.Success -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                }
            }
        }
    }
}

fun Route.deleteTask(
    taskService: TaskService
){
    authenticate {
        delete(path = "/todo/task/delete"){
            val taskId = call.parameters[TASK_ID] ?: ""
            val userId = call.userId
            val task = taskService.getTaskById(taskId)

            if(task == null){
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_NOT_FOUND.message
                    )
                )
                return@delete
            }

            if(task.ownerId != userId){
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ResponseMessages.TASK_DELETE_NOT_ALLOWED.message
                    )
                )
                return@delete
            }

            val deleteTask = taskService.deleteTask(taskId)

            if(!deleteTask){
                call.respond(
                    HttpStatusCode.BadRequest,
                    BasicApiResponse<Unit>(
                        successful = false,
                    )
                )
                return@delete
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = true
                )
            )
        }
    }
}