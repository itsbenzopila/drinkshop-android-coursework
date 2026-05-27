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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit,
    vm: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(vm.events) {
        vm.events.collectLatest { event ->
            when (event) {
                SplashViewModel.SplashEvent.Authenticated -> onAuthenticated()
                SplashViewModel.SplashEvent.Unauthenticated -> onUnauthenticated()
            }
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
