package com.example.data.util.validation.user

sealed class LoginUserValidation{
    object InvalidCredentialsError: LoginUserValidation()
    object EmptyFieldError: LoginUserValidation()
    data class Success(val userId: String?): LoginUserValidation()
}
