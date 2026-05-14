package com.itsbenzopila.drinkshop.domain.model

import java.math.BigDecimal

data class User(
    val id: Long,
    val firebaseUid: String,
    val email: String,
    val fullName: String?,
    val phone: String?,
    val bonusPoints: Int,
)

data class Category(
    val id: Long,
    val name: String,
    val iconUrl: String?,
)

data class Drink(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val volumeMl: Int,
    val inStock: Boolean,
)

data class CartItem(
    val id: Long,
    val drink: Drink,
    val quantity: Int,
    val lineTotal: BigDecimal,
)

data class Cart(
    val items: List<CartItem>,
    val subtotal: BigDecimal,
)

enum class OrderStatus { CREATED, PAID, DELIVERED, CANCELLED }

data class OrderItem(
    val id: Long,
    val drinkId: Long,
    val drinkName: String,
    val quantity: Int,
    val priceAtPurchase: BigDecimal,
)

data class Order(
    val id: Long,
    val totalPrice: BigDecimal,
    val pointsEarned: Int,
    val pointsSpent: Int,
    val status: OrderStatus,
    val createdAt: String,
    val items: List<OrderItem>,
)
