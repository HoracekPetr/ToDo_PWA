package com.example.util

sealed class CreateUserValidation{
    object UserExistsError: CreateUserValidation()
    object EmptyFieldError: CreateUserValidation()
    object Success: CreateUserValidation()
}
