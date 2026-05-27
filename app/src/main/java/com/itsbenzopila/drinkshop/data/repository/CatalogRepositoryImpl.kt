package com.itsbenzopila.drinkshop.data.repository

import com.itsbenzopila.drinkshop.data.local.dao.DrinkDao
import com.itsbenzopila.drinkshop.data.mapper.toDomain
import com.itsbenzopila.drinkshop.data.mapper.toLocal
import com.itsbenzopila.drinkshop.data.remote.api.CatalogApi
import com.itsbenzopila.drinkshop.domain.model.Category
import com.itsbenzopila.drinkshop.domain.model.Drink
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
    private val drinkDao: DrinkDao,
) : CatalogRepository {

    override suspend fun categories(): List<Category> =
        catalogApi.categories().map { it.toDomain() }

    override suspend fun drinks(categoryId: Long?): List<Drink> {
        return try {
            val remoteDrinks = catalogApi.drinks(categoryId).map { it.toDomain() }
            if (categoryId == null) {
                // Cache only "all drinks" for simplicity in this example
                drinkDao.deleteAllDrinks()
                drinkDao.insertDrinks(remoteDrinks.map { it.toLocal() })
            }
            remoteDrinks
        } catch (e: Exception) {
            // Fallback to local cache if offline
            if (categoryId == null) {
                drinkDao.getAllDrinks().first().map { it.toDomain() }
            } else {
                drinkDao.getDrinksByCategory(categoryId).first().map { it.toDomain() }
            }
        }
    }

    override suspend fun drink(id: Long): Drink = catalogApi.drink(id).toDomain()
}
