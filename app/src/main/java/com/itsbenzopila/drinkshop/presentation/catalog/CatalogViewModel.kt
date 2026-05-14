package com.itsbenzopila.drinkshop.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.usecase.AddToCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetCategoriesUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetDrinksUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import com.itsbenzopila.drinkshop.presentation.common.userMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUi(
    val categories: List<Category> = emptyList(),
    val drinks: UiState<List<Drink>> = UiState.Loading,
    val selectedCategoryId: Long? = null,
    val addingDrinkId: Long? = null,
    val snackbar: String? = null,
)

class CatalogViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getDrinksUseCase: GetDrinksUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUi())
    val state: StateFlow<CatalogUi> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(drinks = UiState.Loading) }
            runCatching { getCategoriesUseCase() }
                .onSuccess { cats -> _state.update { it.copy(categories = cats) } }
            runCatching { getDrinksUseCase(_state.value.selectedCategoryId) }
                .onSuccess { ds -> _state.update { it.copy(drinks = UiState.Success(ds)) } }
                .onFailure { t -> _state.update { it.copy(drinks = UiState.Error(t.userMessage())) } }
        }
    }

    fun selectCategory(categoryId: Long?) {
        _state.update { it.copy(selectedCategoryId = categoryId) }
        load()
    }

    fun add(drinkId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(addingDrinkId = drinkId) }
            runCatching { addToCartUseCase(drinkId, 1) }
                .onSuccess { _state.update { it.copy(snackbar = "Добавлено в корзину") } }
                .onFailure { t -> _state.update { it.copy(snackbar = t.userMessage()) } }
            _state.update { it.copy(addingDrinkId = null) }
        }
    }

    fun consumeSnackbar() = _state.update { it.copy(snackbar = null) }
}
