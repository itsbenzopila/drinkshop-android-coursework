package com.itsbenzopila.drinkshop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itsbenzopila.drinkshop.data.local.dao.DrinkDao
import com.itsbenzopila.drinkshop.data.local.entity.DrinkEntity

@Database(entities = [DrinkEntity::class], version = 1, exportSchema = false)
abstract class DrinkDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}
