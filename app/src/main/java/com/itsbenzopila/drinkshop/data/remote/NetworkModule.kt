package com.itsbenzopila.drinkshop.data.remote

import com.itsbenzopila.drinkshop.BuildConfig
import com.itsbenzopila.drinkshop.data.remote.api.AuthApi
import com.itsbenzopila.drinkshop.data.remote.api.CartApi
import com.itsbenzopila.drinkshop.data.remote.api.CatalogApi
import com.itsbenzopila.drinkshop.data.remote.api.OrderApi
import com.itsbenzopila.drinkshop.data.remote.api.UserApi
import com.itsbenzopila.drinkshop.data.remote.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private fun okHttp(): OkHttpClient = OkHttpClient.Builder()
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

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttp())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authApi: AuthApi by lazy { retrofit().create(AuthApi::class.java) }
    val userApi: UserApi by lazy { retrofit().create(UserApi::class.java) }
    val catalogApi: CatalogApi by lazy { retrofit().create(CatalogApi::class.java) }
    val cartApi: CartApi by lazy { retrofit().create(CartApi::class.java) }
    val orderApi: OrderApi by lazy { retrofit().create(OrderApi::class.java) }
}
