package com.itsbenzopila.drinkshop.di

import android.content.Context
import com.itsbenzopila.drinkshop.data.remote.NetworkModule
import com.itsbenzopila.drinkshop.data.repository.AuthRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.CartRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.CatalogRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.OrderRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.UserRepositoryImpl
import com.itsbenzopila.drinkshop.domain.repository.AuthRepository
import com.itsbenzopila.drinkshop.domain.repository.CartRepository
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import com.itsbenzopila.drinkshop.domain.repository.OrderRepository
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import com.itsbenzopila.drinkshop.domain.usecase.AddToCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetCartUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetCategoriesUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetDrinkUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetDrinksUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetMeUseCase
import com.itsbenzopila.drinkshop.domain.usecase.GetOrdersUseCase
import com.itsbenzopila.drinkshop.domain.usecase.PlaceOrderUseCase
import com.itsbenzopila.drinkshop.domain.usecase.RemoveCartItemUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignInUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignOutUseCase
import com.itsbenzopila.drinkshop.domain.usecase.SignUpUseCase
import com.itsbenzopila.drinkshop.domain.usecase.UpdateCartItemUseCase

/**
 * Ручной DI-контейнер для Android. Single source of truth для зависимостей всего приложения.
 */
class AppContainer(@Suppress("unused") private val context: Context) {
    // Repositories
    val authRepository: AuthRepository = AuthRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl(NetworkModule.authApi, NetworkModule.userApi)
    val catalogRepository: CatalogRepository = CatalogRepositoryImpl(NetworkModule.catalogApi)
    val cartRepository: CartRepository = CartRepositoryImpl(NetworkModule.cartApi)
    val orderRepository: OrderRepository = OrderRepositoryImpl(NetworkModule.orderApi)

    // Use cases
    val signInUseCase = SignInUseCase(authRepository, userRepository)
    val signUpUseCase = SignUpUseCase(authRepository, userRepository)
    val signOutUseCase = SignOutUseCase(authRepository)
    val getMeUseCase = GetMeUseCase(userRepository)

    val getCategoriesUseCase = GetCategoriesUseCase(catalogRepository)
    val getDrinksUseCase = GetDrinksUseCase(catalogRepository)
    val getDrinkUseCase = GetDrinkUseCase(catalogRepository)

    val getCartUseCase = GetCartUseCase(cartRepository)
    val addToCartUseCase = AddToCartUseCase(cartRepository)
    val updateCartItemUseCase = UpdateCartItemUseCase(cartRepository)
    val removeCartItemUseCase = RemoveCartItemUseCase(cartRepository)

    val placeOrderUseCase = PlaceOrderUseCase(orderRepository)
    val getOrdersUseCase = GetOrdersUseCase(orderRepository)
}
