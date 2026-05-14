package com.itsbenzopila.drinkshop.data.repository

import com.itsbenzopila.drinkshop.data.mapper.toDomain
import com.itsbenzopila.drinkshop.data.remote.api.CartApi
import com.itsbenzopila.drinkshop.data.remote.dto.AddToCartRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.UpdateCartItemRequestDto
import com.itsbenzopila.drinkshop.domain.model.Cart
import com.itsbenzopila.drinkshop.domain.repository.CartRepository

class CartRepositoryImpl(
    private val cartApi: CartApi,
) : CartRepository {

    override suspend fun get(): Cart = cartApi.get().toDomain()

    override suspend fun add(drinkId: Long, quantity: Int): Cart =
        cartApi.add(AddToCartRequestDto(drinkId, quantity)).toDomain()

    override suspend fun update(itemId: Long, quantity: Int): Cart =
        cartApi.update(itemId, UpdateCartItemRequestDto(quantity)).toDomain()

    override suspend fun remove(itemId: Long): Cart = cartApi.remove(itemId).toDomain()
}
