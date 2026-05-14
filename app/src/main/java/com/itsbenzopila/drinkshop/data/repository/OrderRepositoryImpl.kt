package com.itsbenzopila.drinkshop.data.repository

import com.itsbenzopila.drinkshop.data.mapper.toDomain
import com.itsbenzopila.drinkshop.data.remote.api.OrderApi
import com.itsbenzopila.drinkshop.data.remote.dto.PlaceOrderRequestDto
import com.itsbenzopila.drinkshop.domain.model.Order
import com.itsbenzopila.drinkshop.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val orderApi: OrderApi,
) : OrderRepository {

    override suspend fun list(): List<Order> = orderApi.list().map { it.toDomain() }

    override suspend fun get(id: Long): Order = orderApi.get(id).toDomain()

    override suspend fun place(pointsToSpend: Int): Order =
        orderApi.place(PlaceOrderRequestDto(pointsToSpend)).toDomain()
}
