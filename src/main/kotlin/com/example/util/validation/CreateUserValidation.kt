package com.example.util.validation

sealed class CreateUserValidation{
    object UserExistsError: CreateUserValidation()
    object EmptyFieldError: CreateUserValidation()
    object Success: CreateUserValidation()
}
