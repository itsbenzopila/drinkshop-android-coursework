package com.itsbenzopila.drinkshop.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
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
                // Пытаемся синхронизироваться с таймаутом, чтобы не висеть вечно
                try {
                    withTimeout(5000) {
                        userRepository.sync(fullName = current.displayName)
                    }
                } catch (e: Exception) {
                    // Если сервер недоступен, все равно пускаем в приложение
                    // (будут работать данные из Room/кэша)
                    e.printStackTrace()
                }
                _events.emit(SplashEvent.Authenticated)
            }
        }
    }

    sealed interface SplashEvent {
        data object Authenticated : SplashEvent
        data object Unauthenticated : SplashEvent
    }
}
