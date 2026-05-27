package com.itsbenzopila.drinkshop.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.model.User
import com.itsbenzopila.drinkshop.domain.usecase.GetMeUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignOutUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUi(
    val user: UiState<User> = UiState.Loading,
    val signedOut: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMeUseCase: GetMeUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUi())
    val state: StateFlow<ProfileUi> = _state

    init {
        viewModelScope.launch {
            runCatching { getMeUseCase() }
                .onSuccess { u -> _state.update { it.copy(user = UiState.Success(u)) } }
                .onFailure { t -> _state.update { it.copy(user = UiState.Error(t.userMessage())) } }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _state.update { it.copy(signedOut = true) }
        }
    }
}
