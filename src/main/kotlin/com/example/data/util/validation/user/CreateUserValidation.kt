package com.example.data.util.validation.user

sealed class CreateUserValidation{
    object UserExistsError: CreateUserValidation()
    object EmptyFieldError: CreateUserValidation()
    data class Success(val request: Boolean): CreateUserValidation()
}
