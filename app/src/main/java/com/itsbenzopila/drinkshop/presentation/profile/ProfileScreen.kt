package com.itsbenzopila.drinkshop.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.di.AppContainer
import com.itsbenzopila.drinkshop.domain.model.User
import com.itsbenzopila.drinkshop.domain.usecase.GetMeUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignOutUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class ProfileUi(
    val user: UiState<User> = UiState.Loading,
    val signedOut: Boolean = false,
)

private class ProfileViewModel(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    container: AppContainer,
    onSignedOut: () -> Unit,
) {
    val vm = remember { ProfileViewModel(container.getMeUseCase, container.signOutUseCase) }
    val state by vm.state.collectAsState()

    LaunchedEffect(state.signedOut) {
        if (state.signedOut) onSignedOut()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Профиль") }) }) { padding ->
        when (val s = state.user) {
            is UiState.Loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            is UiState.Success -> ProfileContent(s.data, padding, onSignOut = vm::signOut)
        }
    }
}

@Composable
private fun ProfileContent(user: User, padding: PaddingValues, onSignOut: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Бонусные баллы", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = user.bonusPoints.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "1 балл = 1 ₽, можно списать до 30 % от заказа",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Text("Email: ${user.email}")
        user.fullName?.let { Text("Имя: $it") }
        user.phone?.let { Text("Телефон: $it") }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onSignOut,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Выйти")
        }
    }
}
