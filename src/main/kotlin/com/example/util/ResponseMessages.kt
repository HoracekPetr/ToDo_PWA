package com.example.util

enum class ResponseMessages(val message: String){
    USER_EXISTS(message = "Username is already taken!"),
    FIELDS_BLANK(message = "Fields can't be empty!")
}
