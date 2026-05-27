package com.itsbenzopila.drinkshop.di

import com.itsbenzopila.drinkshop.data.repository.AuthRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.CartRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.CatalogRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.OrderRepositoryImpl
import com.itsbenzopila.drinkshop.data.repository.UserRepositoryImpl
import com.itsbenzopila.drinkshop.domain.repository.AuthRepository
import com.itsbenzopila.drinkshop.domain.repository.CartRepository
import com.itsbenzopila.drinkshop.domain.repository.CatalogRepository
import com.itsbenzopila.drinkshop.domain.repository.OrderRepository
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
