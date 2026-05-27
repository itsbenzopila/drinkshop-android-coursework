package com.itsbenzopila.drinkshop.presentation.catalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.usecase.AddToCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetDrinkUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import com.itsbenzopila.drinkshop.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUi(
    val drink: UiState<Drink> = UiState.Loading,
    val isAdding: Boolean = false,
)

@HiltViewModel
class DrinkDetailViewModel @Inject constructor(
    private val getDrinkUseCase: GetDrinkUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val drinkId: Long = checkNotNull(savedStateHandle[Screen.DrinkDetail.ARG])
    private val _state = MutableStateFlow(DetailUi())
    val state: StateFlow<DetailUi> = _state

    init {
        viewModelScope.launch {
            runCatching { getDrinkUseCase(drinkId) }
                .onSuccess { d -> _state.update { it.copy(drink = UiState.Success(d)) } }
                .onFailure { t -> _state.update { it.copy(drink = UiState.Error(t.userMessage())) } }
        }
    }

    fun add(onDone: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isAdding = true) }
            runCatching { addToCartUseCase(drinkId, 1) }
                .onSuccess { onDone() }
            _state.update { it.copy(isAdding = false) }
        }
    }
}
