package com.example.data.util.task

enum class UrgencyType(val value: Int){
    LOW(value = 0),
    MEDIUM(value = 1),
    HIGH(value = 2)
}


sealed class Urgency(val urgency: Int) {
    object Low: Urgency(urgency = UrgencyType.LOW.value)
    object Medium: Urgency(urgency = UrgencyType.MEDIUM.value)
    object High: Urgency(urgency = UrgencyType.HIGH.value)

    fun toUrgency(inputValue: Int) {
        when (inputValue) {
            UrgencyType.LOW.value -> Low
            UrgencyType.MEDIUM.value -> Medium
            UrgencyType.HIGH.value -> High
            else -> Medium
        }
    }
}