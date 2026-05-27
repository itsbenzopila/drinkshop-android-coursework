package com.itsbenzopila.drinkshop.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.model.Cart
import com.itsbenzopila.drinkshop.domain.model.User
import com.itsbenzopila.drinkshop.domain.usecase.GetCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetMeUseCase
import com.itsbenzopila.drinkshop.domain.usecase.PlaceOrderUseCase
import com.itsbenzopila.drinkshop.domain.usecase.RemoveCartItemUseCase
import com.itsbenzopila.drinkshop.domain.usecase.UpdateCartItemUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

data class CartUi(
    val cart: UiState<Cart> = UiState.Loading,
    val user: User? = null,
    val pointsToSpend: Int = 0,
    val isPlacing: Boolean = false,
    val snackbar: String? = null,
    val placed: Boolean = false,
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val getMeUseCase: GetMeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CartUi())
    val state: StateFlow<CartUi> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(cart = UiState.Loading) }
            runCatching { getCartUseCase() }
                .onSuccess { c -> _state.update { it.copy(cart = UiState.Success(c)) } }
                .onFailure { t -> _state.update { it.copy(cart = UiState.Error(t.userMessage())) } }
            runCatching { getMeUseCase() }
                .onSuccess { u -> _state.update { it.copy(user = u) } }
        }
    }

    fun changeQuantity(itemId: Long, quantity: Int) {
        if (quantity < 1) {
            remove(itemId)
            return
        }
        viewModelScope.launch {
            runCatching { updateCartItemUseCase(itemId, quantity) }
                .onSuccess { c -> _state.update { it.copy(cart = UiState.Success(c)) } }
                .onFailure { t -> _state.update { it.copy(snackbar = t.userMessage()) } }
        }
    }

    fun remove(itemId: Long) {
        viewModelScope.launch {
            runCatching { removeCartItemUseCase(itemId) }
                .onSuccess { c -> _state.update { it.copy(cart = UiState.Success(c)) } }
                .onFailure { t -> _state.update { it.copy(snackbar = t.userMessage()) } }
        }
    }

    fun setPoints(value: Int) {
        val current = _state.value
        val sub = (current.cart as? UiState.Success)?.data?.subtotal ?: BigDecimal.ZERO
        val maxByOrder = sub.multiply(BigDecimal("0.30")).setScale(0, RoundingMode.DOWN).toInt()
        val maxByUser = current.user?.bonusPoints ?: 0
        val clamped = value.coerceIn(0, minOf(maxByOrder, maxByUser))
        _state.update { it.copy(pointsToSpend = clamped) }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _state.update { it.copy(isPlacing = true) }
            runCatching { placeOrderUseCase(_state.value.pointsToSpend) }
                .onSuccess { order ->
                    _state.update {
                        it.copy(
                            isPlacing = false,
                            placed = true,
                            snackbar = "Заказ оформлен. Начислено баллов: ${order.pointsEarned}",
                        )
                    }
                }
                .onFailure { t ->
                    _state.update { it.copy(isPlacing = false, snackbar = t.userMessage()) }
                }
        }
    }

    fun consumeSnackbar() = _state.update { it.copy(snackbar = null) }
    fun consumePlaced() = _state.update { it.copy(placed = false) }
}
