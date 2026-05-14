package com.itsbenzopila.drinkshop.domain.usecase

import com.itsbenzopila.drinkshop.domain.repository.AuthRepository
import com.itsbenzopila.drinkshop.domain.repository.CartRepository
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import com.itsbenzopila.drinkshop.domain.repository.OrderRepository
import com.itsbenzopila.drinkshop.domain.repository.UserRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> = runCatching {
        authRepository.signIn(email, password).getOrThrow()
        userRepository.sync(fullName = null)
    }
}

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String, fullName: String): Result<Unit> = runCatching {
        authRepository.signUp(email, password, fullName).getOrThrow()
        userRepository.sync(fullName)
    }
}

class SignOutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.signOut()
}

class GetMeUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.me()
}

class GetCategoriesUseCase(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke() = catalogRepository.categories()
}

class GetDrinksUseCase(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke(categoryId: Long?) = catalogRepository.drinks(categoryId)
}

class GetDrinkUseCase(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke(id: Long) = catalogRepository.drink(id)
}

class GetCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke() = cartRepository.get()
}

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(drinkId: Long, quantity: Int = 1) = cartRepository.add(drinkId, quantity)
}

class UpdateCartItemUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(itemId: Long, quantity: Int) = cartRepository.update(itemId, quantity)
}

class RemoveCartItemUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(itemId: Long) = cartRepository.remove(itemId)
}

class PlaceOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(pointsToSpend: Int) = orderRepository.place(pointsToSpend)
}

class GetOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke() = orderRepository.list()
}
