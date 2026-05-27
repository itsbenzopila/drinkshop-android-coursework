package com.itsbenzopila.drinkshop.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.usecase.SignInUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignUpUseCase
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val signedIn: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun onEmail(value: String) = _state.update { it.copy(email = value, error = null) }
    fun onPassword(value: String) = _state.update { it.copy(password = value, error = null) }
    fun onFullName(value: String) = _state.update { it.copy(fullName = value, error = null) }

    fun signIn() {
        val (email, password) = _state.value.email.trim() to _state.value.password
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Введите email и пароль") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = signInUseCase(email, password)
            _state.update {
                if (result.isSuccess) it.copy(isLoading = false, signedIn = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.userMessage())
            }
        }
    }

    fun signUp() {
        val email = _state.value.email.trim()
        val password = _state.value.password
        val fullName = _state.value.fullName.trim()
        if (email.isBlank() || password.length < 6 || fullName.isBlank()) {
            _state.update { it.copy(error = "Email, имя и пароль (от 6 символов) обязательны") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = signUpUseCase(email, password, fullName)
            _state.update {
                if (result.isSuccess) it.copy(isLoading = false, signedIn = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.userMessage())
            }
        }
    }
}
