package com.itsbenzopila.drinkshop.presentation.common

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

fun Throwable.userMessage(): String =
    message?.takeIf { it.isNotBlank() } ?: "Что-то пошло не так"
