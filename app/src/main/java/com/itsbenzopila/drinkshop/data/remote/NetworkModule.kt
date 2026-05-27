package com.itsbenzopila.drinkshop.data.remote

import com.itsbenzopila.drinkshop.BuildConfig
import com.itsbenzopila.drinkshop.data.remote.api.AuthApi
import com.itsbenzopila.drinkshop.data.remote.api.CartApi
import com.itsbenzopila.drinkshop.data.remote.api.CatalogApi
import com.itsbenzopila.drinkshop.data.remote.api.OrderApi
import com.itsbenzopila.drinkshop.data.remote.api.UserApi
import com.itsbenzopila.drinkshop.data.remote.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            }
        )
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi = retrofit.create(CatalogApi::class.java)

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi = retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi = retrofit.create(OrderApi::class.java)
}
