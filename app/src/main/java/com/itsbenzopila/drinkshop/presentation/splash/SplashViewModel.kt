package com.itsbenzopila.drinkshop.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _events = MutableSharedFlow<SplashEvent>()
    val events = _events.asSharedFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val current = FirebaseAuth.getInstance().currentUser
            if (current == null) {
                _events.emit(SplashEvent.Unauthenticated)
            } else {
                runCatching { userRepository.sync(fullName = current.displayName) }
                _events.emit(SplashEvent.Authenticated)
            }
        }
    }

    sealed interface SplashEvent {
        data object Authenticated : SplashEvent
        data object Unauthenticated : SplashEvent
    }
}
