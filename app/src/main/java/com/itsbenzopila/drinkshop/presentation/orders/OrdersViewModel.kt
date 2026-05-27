package com.itsbenzopila.drinkshop.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.model.Order
import com.itsbenzopila.drinkshop.domain.usecase.GetOrdersUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Order>>> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            runCatching { getOrdersUseCase() }
                .onSuccess { orders -> _state.value = UiState.Success(orders) }
                .onFailure { t -> _state.value = UiState.Error(t.userMessage()) }
        }
    }
}
