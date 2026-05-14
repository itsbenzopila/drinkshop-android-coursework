package com.itsbenzopila.drinkshop.data.remote.api

import com.itsbenzopila.drinkshop.data.remote.dto.AddToCartRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.CartDto
import com.itsbenzopila.drinkshop.data.remote.dto.CategoryDto
import com.itsbenzopila.drinkshop.data.remote.dto.DrinkDto
import com.itsbenzopila.drinkshop.data.remote.dto.OrderDto
import com.itsbenzopila.drinkshop.data.remote.dto.PlaceOrderRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.SyncRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.UpdateCartItemRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.UpdateProfileRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST("api/v1/auth/sync")
    suspend fun sync(@Body body: SyncRequestDto): UserDto
}

interface UserApi {
    @GET("api/v1/users/me")
    suspend fun me(): UserDto

    @PATCH("api/v1/users/me")
    suspend fun updateProfile(@Body body: UpdateProfileRequestDto): UserDto
}

interface CatalogApi {
    @GET("api/v1/categories")
    suspend fun categories(): List<CategoryDto>

    @GET("api/v1/drinks")
    suspend fun drinks(@Query("categoryId") categoryId: Long? = null): List<DrinkDto>

    @GET("api/v1/drinks/{id}")
    suspend fun drink(@Path("id") id: Long): DrinkDto
}

interface CartApi {
    @GET("api/v1/cart")
    suspend fun get(): CartDto

    @POST("api/v1/cart/items")
    suspend fun add(@Body body: AddToCartRequestDto): CartDto

    @PATCH("api/v1/cart/items/{id}")
    suspend fun update(@Path("id") id: Long, @Body body: UpdateCartItemRequestDto): CartDto

    @DELETE("api/v1/cart/items/{id}")
    suspend fun remove(@Path("id") id: Long): CartDto
}

interface OrderApi {
    @GET("api/v1/orders")
    suspend fun list(): List<OrderDto>

    @GET("api/v1/orders/{id}")
    suspend fun get(@Path("id") id: Long): OrderDto

    @POST("api/v1/orders")
    suspend fun place(@Body body: PlaceOrderRequestDto): OrderDto
}
