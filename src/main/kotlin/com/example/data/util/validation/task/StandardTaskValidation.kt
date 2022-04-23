package com.example.data.util.validation.task

sealed class StandardTaskValidation{
    object EmptyTitleError: StandardTaskValidation()
    object TitleTooLongError: StandardTaskValidation()
    object InvalidUrgencyValueError: StandardTaskValidation()
    data class Success(val request: Boolean): StandardTaskValidation()
}
