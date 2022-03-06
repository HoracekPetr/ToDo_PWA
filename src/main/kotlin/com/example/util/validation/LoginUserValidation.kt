package com.example.util.validation

sealed class LoginUserValidation{
    object InvalidCredentialsError: LoginUserValidation()
    object EmptyFieldError: LoginUserValidation()
    object Success: LoginUserValidation()
}
