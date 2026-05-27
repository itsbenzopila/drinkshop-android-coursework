package com.itsbenzopila.drinkshop.data.mapper

import com.itsbenzopila.drinkshop.data.local.entity.DrinkEntity
import com.itsbenzopila.drinkshop.data.remote.dto.CartDto
import com.itsbenzopila.drinkshop.data.remote.dto.CartItemDto
import com.itsbenzopila.drinkshop.data.remote.dto.CategoryDto
import com.itsbenzopila.drinkshop.data.remote.dto.DrinkDto
import com.itsbenzopila.drinkshop.data.remote.dto.OrderDto
import com.itsbenzopila.drinkshop.data.remote.dto.OrderItemDto
import com.itsbenzopila.drinkshop.data.remote.dto.UserDto
import com.itsbenzopila.drinkshop.domain.model.Cart
import com.itsbenzopila.drinkshop.domain.model.CartItem
import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.model.Order
import com.itsbenzopila.drinkshop.domain.model.OrderItem
import com.itsbenzopila.drinkshop.domain.model.OrderStatus
import com.itsbenzopila.drinkshop.domain.model.User
import java.math.BigDecimal

fun UserDto.toDomain() = User(
    id = id,
    firebaseUid = firebaseUid,
    email = email,
    fullName = fullName,
    phone = phone,
    bonusPoints = bonusPoints,
)

fun CategoryDto.toDomain() = Category(id = id, name = name, iconUrl = iconUrl)

fun DrinkDto.toDomain() = Drink(
    id = id,
    categoryId = categoryId,
    name = name,
    description = description,
    price = BigDecimal(price),
    imageUrl = imageUrl,
    volumeMl = volumeMl,
    inStock = inStock,
)

fun CartItemDto.toDomain() = CartItem(
    id = id,
    drink = drink.toDomain(),
    quantity = quantity,
    lineTotal = BigDecimal(lineTotal),
)

fun CartDto.toDomain() = Cart(
    items = items.map { it.toDomain() },
    subtotal = BigDecimal(subtotal),
)

fun OrderItemDto.toDomain() = OrderItem(
    id = id,
    drinkId = drinkId,
    drinkName = drinkName,
    quantity = quantity,
    priceAtPurchase = BigDecimal(priceAtPurchase),
)

fun OrderDto.toDomain() = Order(
    id = id,
    totalPrice = BigDecimal(totalPrice),
    pointsEarned = pointsEarned,
    pointsSpent = pointsSpent,
    status = runCatching { OrderStatus.valueOf(status) }.getOrDefault(OrderStatus.CREATED),
    createdAt = createdAt,
    items = items.map { it.toDomain() },
)

fun Drink.toLocal() = DrinkEntity(
    id = id,
    categoryId = categoryId,
    name = name,
    description = description,
    price = price.toPlainString(),
    imageUrl = imageUrl,
    volumeMl = volumeMl,
    inStock = inStock
)

fun DrinkEntity.toDomain() = Drink(
    id = id,
    categoryId = categoryId,
    name = name,
    description = description,
    price = BigDecimal(price),
    imageUrl = imageUrl,
    volumeMl = volumeMl,
    inStock = inStock
)
