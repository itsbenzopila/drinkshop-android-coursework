package com.itsbenzopila.drinkshop.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.itsbenzopila.drinkshop.di.AppContainer
import com.itsbenzopila.drinkshop.domain.model.Cart
import com.itsbenzopila.drinkshop.domain.model.CartItem
import com.itsbenzopila.drinkshop.presentation.common.UiState
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    container: AppContainer,
    onOrderPlaced: () -> Unit,
) {
    val vm = remember {
        CartViewModel(
            container.getCartUseCase,
            container.updateCartItemUseCase,
            container.removeCartItemUseCase,
            container.placeOrderUseCase,
            container.getMeUseCase,
        )
    }
    val state by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.snackbar) {
        state.snackbar?.let {
            snackbar.showSnackbar(it)
            vm.consumeSnackbar()
        }
    }
    LaunchedEffect(state.placed) {
        if (state.placed) {
            vm.consumePlaced()
            onOrderPlaced()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Корзина") }) },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        when (val s = state.cart) {
            is UiState.Loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
            }
            is UiState.Success -> CartContent(
                cart = s.data,
                bonusPoints = state.user?.bonusPoints ?: 0,
                pointsToSpend = state.pointsToSpend,
                isPlacing = state.isPlacing,
                padding = padding,
                onChangeQty = vm::changeQuantity,
                onRemove = vm::remove,
                onPointsChange = vm::setPoints,
                onPlace = vm::placeOrder,
            )
        }
    }
}

@Composable
private fun CartContent(
    cart: Cart,
    bonusPoints: Int,
    pointsToSpend: Int,
    isPlacing: Boolean,
    padding: PaddingValues,
    onChangeQty: (Long, Int) -> Unit,
    onRemove: (Long) -> Unit,
    onPointsChange: (Int) -> Unit,
    onPlace: () -> Unit,
) {
    if (cart.items.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Корзина пуста", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val maxPoints = remember(cart.subtotal, bonusPoints) {
        val byOrder = cart.subtotal.multiply(BigDecimal("0.30")).setScale(0, RoundingMode.DOWN).toInt()
        minOf(byOrder, bonusPoints)
    }
    val total = remember(cart.subtotal, pointsToSpend) {
        (cart.subtotal - BigDecimal(pointsToSpend)).coerceAtLeast(BigDecimal.ZERO)
    }

    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(cart.items, key = { it.id }) { item ->
                CartRow(item, onChangeQty, onRemove)
            }
        }
        HorizontalDivider()
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Бонусные баллы: $bonusPoints", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = pointsToSpend.toString(),
                onValueChange = { v -> onPointsChange(v.toIntOrNull() ?: 0) },
                label = { Text("Списать (макс $maxPoints, 1 балл = 1 ₽)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Row {
                Text("Сумма")
                Spacer(Modifier.weight(1f))
                Text("${cart.subtotal.toPlainString()} ₽")
            }
            Row {
                Text("Скидка баллами")
                Spacer(Modifier.weight(1f))
                Text("− $pointsToSpend ₽")
            }
            Row {
                Text("К оплате", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Text(
                    "${total.toPlainString()} ₽",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Button(
                onClick = onPlace,
                enabled = !isPlacing && cart.items.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isPlacing) CircularProgressIndicator(modifier = Modifier.height(20.dp))
                else Text("Оформить заказ")
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    onChangeQty: (Long, Int) -> Unit,
    onRemove: (Long) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.drink.name, style = MaterialTheme.typography.titleSmall)
                Text("${item.drink.price.toPlainString()} ₽ × ${item.quantity}", style = MaterialTheme.typography.bodySmall)
                Text("${item.lineTotal.toPlainString()} ₽", fontWeight = FontWeight.SemiBold)
            }
            IconButton(onClick = { onChangeQty(item.id, item.quantity - 1) }) { Text("−") }
            Text(item.quantity.toString())
            IconButton(onClick = { onChangeQty(item.id, item.quantity + 1) }) { Text("+") }
            IconButton(onClick = { onRemove(item.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}
