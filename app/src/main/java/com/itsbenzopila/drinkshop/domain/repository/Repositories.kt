package com.itsbenzopila.drinkshop.domain.repository

import com.itsbenzopila.drinkshop.domain.model.Cart
import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.model.Order
import com.itsbenzopila.drinkshop.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isSignedIn: Flow<Boolean>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, fullName: String?): Result<Unit>
    suspend fun signOut()
    suspend fun currentIdToken(forceRefresh: Boolean = false): String?
}

interface UserRepository {
    /** После логина Firebase синхронизируем профиль с бэкендом. */
    suspend fun sync(fullName: String?): User
    suspend fun me(): User
    suspend fun updateProfile(fullName: String?, phone: String?): User
}

interface CatalogRepository {
    suspend fun categories(): List<Category>
    suspend fun drinks(categoryId: Long? = null): List<Drink>
    suspend fun drink(id: Long): Drink
}

interface CartRepository {
    suspend fun get(): Cart
    suspend fun add(drinkId: Long, quantity: Int): Cart
    suspend fun update(itemId: Long, quantity: Int): Cart
    suspend fun remove(itemId: Long): Cart
}

interface OrderRepository {
    suspend fun list(): List<Order>
    suspend fun get(id: Long): Order
    suspend fun place(pointsToSpend: Int): Order
}
