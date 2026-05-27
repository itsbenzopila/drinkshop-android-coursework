package com.itsbenzopila.drinkshop.presentation.catalog

import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.usecase.AddToCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetCategoriesUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetDrinksUseCase
import com.itsbenzopila.drinkshop.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val getCategoriesUseCase = mock(GetCategoriesUseCase::class.java)
    private val getDrinksUseCase = mock(GetDrinksUseCase::class.java)
    private val addToCartUseCase = mock(AddToCartUseCase::class.java)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load should update state with categories and drinks`() = runTest {
        val categories = listOf(Category(1, "Coffee", null))
        val drinks = listOf(
            Drink(1, 1, "Latte", "Good", BigDecimal("100"), null, 300, true)
        )

        `when`(getCategoriesUseCase()).thenReturn(categories)
        `when`(getDrinksUseCase(null)).thenReturn(drinks)

        val viewModel = CatalogViewModel(getCategoriesUseCase, getDrinksUseCase, addToCartUseCase)
        
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(categories, state.categories)
        assertTrue(state.drinks is UiState.Success)
        assertEquals(drinks, (state.drinks as UiState.Success).data)
    }
}
