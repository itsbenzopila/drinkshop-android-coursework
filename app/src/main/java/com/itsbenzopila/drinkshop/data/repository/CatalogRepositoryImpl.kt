package com.itsbenzopila.drinkshop.data.repository

import com.itsbenzopila.drinkshop.data.mapper.toDomain
import com.itsbenzopila.drinkshop.data.remote.api.CatalogApi
import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
) : CatalogRepository {

    override suspend fun categories(): List<Category> =
        catalogApi.categories().map { it.toDomain() }

    override suspend fun drinks(categoryId: Long?): List<Drink> =
        catalogApi.drinks(categoryId).map { it.toDomain() }

    override suspend fun drink(id: Long): Drink = catalogApi.drink(id).toDomain()
}
