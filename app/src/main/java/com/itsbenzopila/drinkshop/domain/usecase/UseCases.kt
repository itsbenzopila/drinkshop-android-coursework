package com.itsbenzopila.drinkshop.domain.usecase

import com.itsbenzopila.drinkshop.domain.repository.AuthRepository
import com.itsbenzopila.drinkshop.domain.repository.CartRepository
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import com.itsbenzopila.drinkshop.domain.repository.OrderRepository
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> = runCatching {
        authRepository.signIn(email, password).getOrThrow()
        userRepository.sync(fullName = null)
    }
}

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String, fullName: String): Result<Unit> = runCatching {
        authRepository.signUp(email, password, fullName).getOrThrow()
        userRepository.sync(fullName)
    }
}

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.signOut()
}

class GetMeUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.me()
}

class GetCategoriesUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke() = catalogRepository.categories()
}

class GetDrinksUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke(categoryId: Long?) = catalogRepository.drinks(categoryId)
}

class GetDrinkUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {
    suspend operator fun invoke(id: Long) = catalogRepository.drink(id)
}

class GetCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke() = cartRepository.get()
}

class AddToCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(drinkId: Long, quantity: Int = 1) = cartRepository.add(drinkId, quantity)
}

class UpdateCartItemUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(itemId: Long, quantity: Int) = cartRepository.update(itemId, quantity)
}

class RemoveCartItemUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke(itemId: Long) = cartRepository.remove(itemId)
}

class PlaceOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(pointsToSpend: Int) = orderRepository.place(pointsToSpend)
}

class GetOrdersUseCase @Inject constructor(private val orderRepository: OrderRepository) {
    suspend operator fun invoke() = orderRepository.list()
}
