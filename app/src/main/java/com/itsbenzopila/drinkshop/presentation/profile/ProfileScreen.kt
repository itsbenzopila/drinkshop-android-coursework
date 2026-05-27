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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsbenzopila.drinkshop.domain.model.User
import com.itsbenzopila.drinkshop.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignedOut: () -> Unit,
    vm: ProfileViewModel = hiltViewModel(),
) {
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
