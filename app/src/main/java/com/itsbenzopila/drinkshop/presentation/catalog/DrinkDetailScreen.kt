package com.itsbenzopila.drinkshop.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.itsbenzopila.drinkshop.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkDetailScreen(
    drinkId: Long,
    onBack: () -> Unit,
    onAdded: () -> Unit,
    vm: DrinkDetailViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Напиток") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { padding ->
        when (val s = state.drink) {
            is UiState.Loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            is UiState.Success -> {
                val d = s.data
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.4f), contentAlignment = Alignment.Center) {
                        if (!d.imageUrl.isNullOrBlank()) {
                            AsyncImage(model = d.imageUrl, contentDescription = d.name, modifier = Modifier.fillMaxSize())
                        } else {
                            Text("☕", style = MaterialTheme.typography.displayLarge)
                        }
                    }
                    Text(d.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Text("${d.volumeMl} мл", style = MaterialTheme.typography.bodyMedium)
                    Text(d.description.ifBlank { "Описание появится позже." }, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${d.price.toPlainString()} ₽",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { vm.add(onAdded) },
                        enabled = d.inStock && !state.isAdding,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (state.isAdding) CircularProgressIndicator(modifier = Modifier.height(20.dp))
                        else Text(if (d.inStock) "Добавить в корзину" else "Нет в наличии")
                    }
                }
            }
        }
    }
}
