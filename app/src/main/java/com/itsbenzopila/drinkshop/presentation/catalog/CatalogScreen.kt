package com.itsbenzopila.drinkshop.presentation.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onOpenDrink: (Long) -> Unit,
    vm: CatalogViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.snackbar) {
        state.snackbar?.let {
            snackbar.showSnackbar(it)
            vm.consumeSnackbar()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Каталог") }) },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = state.selectedCategoryId == null,
                        onClick = { vm.selectCategory(null) },
                        label = { Text("Все") },
                    )
                }
                items(state.categories, key = { it.id }) { cat ->
                    FilterChip(
                        selected = state.selectedCategoryId == cat.id,
                        onClick = { vm.selectCategory(cat.id) },
                        label = { Text(cat.name) },
                    )
                }
            }

            when (val s = state.drinks) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                }
                is UiState.Success -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(s.data, key = { it.id }) { drink ->
                        DrinkCard(
                            drink = drink,
                            isAdding = state.addingDrinkId == drink.id,
                            onClick = { onOpenDrink(drink.id) },
                            onAdd = { vm.add(drink.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrinkCard(
    drink: Drink,
    isAdding: Boolean,
    onClick: () -> Unit,
    onAdd: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
                if (!drink.imageUrl.isNullOrBlank()) {
                    AsyncImage(model = drink.imageUrl, contentDescription = drink.name, modifier = Modifier.fillMaxSize())
                } else {
                    Text("☕", style = MaterialTheme.typography.displayMedium)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(drink.name, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Text("${drink.volumeMl} мл", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${drink.price.toPlainString()} ₽",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.weight(1f))
                Button(onClick = onAdd, enabled = drink.inStock && !isAdding) {
                    if (isAdding) {
                        CircularProgressIndicator(modifier = Modifier.height(18.dp))
                    } else {
                        Text(if (drink.inStock) "+" else "—")
                    }
                }
            }
        }
    }
}
