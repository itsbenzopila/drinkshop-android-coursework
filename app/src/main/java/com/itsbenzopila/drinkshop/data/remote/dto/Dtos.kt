package com.itsbenzopila.drinkshop.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SyncRequestDto(val fullName: String? = null)

@Serializable
data class UpdateProfileRequestDto(val fullName: String? = null, val phone: String? = null)

@Serializable
data class UserDto(
    val id: Long,
    val firebaseUid: String,
    val email: String,
    val fullName: String? = null,
    val phone: String? = null,
    val bonusPoints: Int,
    val createdAt: String,
)

@Serializable
data class CategoryDto(
    val id: Long,
    val name: String,
    val iconUrl: String? = null,
)

@Serializable
data class DrinkDto(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String? = null,
    val volumeMl: Int,
    val inStock: Boolean,
)

@Serializable
data class CartItemDto(
    val id: Long,
    val drink: DrinkDto,
    val quantity: Int,
    val lineTotal: String,
)

@Serializable
data class CartDto(
    val items: List<CartItemDto>,
    val subtotal: String,
)

@Serializable
data class AddToCartRequestDto(val drinkId: Long, val quantity: Int = 1)

@Serializable
data class UpdateCartItemRequestDto(val quantity: Int)

@Serializable
data class PlaceOrderRequestDto(val pointsToSpend: Int = 0)

@Serializable
data class OrderItemDto(
    val id: Long,
    val drinkId: Long,
    val drinkName: String,
    val quantity: Int,
    val priceAtPurchase: String,
)

@Serializable
data class OrderDto(
    val id: Long,
    val totalPrice: String,
    val pointsEarned: Int,
    val pointsSpent: Int,
    val status: String,
    val createdAt: String,
    val items: List<OrderItemDto>,
)

@Serializable
data class ErrorResponseDto(val code: String, val message: String)
