package com.itsbenzopila.drinkshop.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.itsbenzopila.drinkshop.domain.model.Order
import com.itsbenzopila.drinkshop.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(vm: OrdersViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Заказы") }) }) { padding ->
        when (val s = state) {
            is UiState.Loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            is UiState.Success -> {
                if (s.data.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("Заказов пока нет")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(s.data, key = { it.id }) { order -> OrderCard(order) }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(14.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Заказ #${order.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
            }
            Text(
                order.createdAt.substringBefore('T').split('-').reversed().joinToString("."),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.padding(top = 8.dp))
            order.items.forEach { item ->
                Row {
                    Text(item.drinkName)
                    Text("  × ${item.quantity}")
                    Spacer(Modifier.weight(1f))
                    Text("${item.priceAtPurchase.toPlainString()} ₽")
                }
            }
            Spacer(Modifier.padding(top = 8.dp))
            Row {
                Text("Итого", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Text("${order.totalPrice.toPlainString()} ₽", fontWeight = FontWeight.SemiBold)
            }
            Row {
                Text("Баллы: списано ${order.pointsSpent}, начислено ${order.pointsEarned}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
