package com.itsbenzopila.drinkshop.presentation.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.itsbenzopila.drinkshop.di.AppContainer

@Composable
fun SplashScreen(
    container: AppContainer,
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit,
) {
    LaunchedEffect(Unit) {
        val current = FirebaseAuth.getInstance().currentUser
        if (current == null) {
            onUnauthenticated()
        } else {
            // токен живой → синкуем пользователя с бэком и идём в каталог
            runCatching { container.userRepository.sync(fullName = current.displayName) }
            onAuthenticated()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Drinkshop")
        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
    }
}
