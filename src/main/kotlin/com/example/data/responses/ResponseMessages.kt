package com.example.data.responses

import com.example.data.util.validation.ValidationConstants.TITLE_CHAR_LIMIT

enum class ResponseMessages(val message: String){
    USER_EXISTS(message = "Username is already taken!"),
    INVALID_CREDENTIALS(message = "Username or password is not correct!"),

    FIELDS_BLANK(message = "Fields can't be empty!"),
    TITLE_BLANK(message = "Title can't be empty!"),
    TITLE_TOO_LONG(message = "Title can't exceed $TITLE_CHAR_LIMIT characters!"),
    INVALID_URGENCY(message = "Invalid urgency value!"),

    NO_TASKS(message = "No tasks were found :("),
    NO_COMPLETED_TASKS(message = "No completed tasks were found :("),
    NO_UNCOMPLETED_TASKS(message = "No uncompleted tasks were found :)"),
    TASK_NOT_FOUND(message = "The task wasn't found!"),
    TASK_NOT_ALLOWED(message = "You are not allowed to see tasks you are not an owner of!"),
    TASK_CHANGE_NOT_ALLOWED(message = "You are not allowed to change tasks you are not an owner of!"),
    TASK_DELETE_NOT_ALLOWED(message = "You are not allowed to delete tasks you are not an owner of!")

}
