package com.itsbenzopila.drinkshop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drinks")
data class DrinkEntity(
    @PrimaryKey val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String?,
    val volumeMl: Int,
    val inStock: Boolean
)
