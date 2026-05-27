package com.itsbenzopila.drinkshop.di

import android.content.Context
import androidx.room.Room
import com.itsbenzopila.drinkshop.data.local.DrinkDatabase
import com.itsbenzopila.drinkshop.data.local.dao.DrinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DrinkDatabase {
        return Room.databaseBuilder(
            context,
            DrinkDatabase::class.java,
            "drink_shop.db"
        ).build()
    }

    @Provides
    fun provideDrinkDao(database: DrinkDatabase): DrinkDao {
        return database.drinkDao()
    }
}
