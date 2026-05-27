package com.itsbenzopila.drinkshop.data.repository

import com.itsbenzopila.drinkshop.data.mapper.toDomain
import com.itsbenzopila.drinkshop.data.remote.api.AuthApi
import com.itsbenzopila.drinkshop.data.remote.api.UserApi
import com.itsbenzopila.drinkshop.data.remote.dto.SyncRequestDto
import com.itsbenzopila.drinkshop.data.remote.dto.UpdateProfileRequestDto
import com.itsbenzopila.drinkshop.domain.model.User
import com.itsbenzopila.drinkshop.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
) : UserRepository {

    override suspend fun sync(fullName: String?): User =
        authApi.sync(SyncRequestDto(fullName)).toDomain()

    override suspend fun me(): User = userApi.me().toDomain()

    override suspend fun updateProfile(fullName: String?, phone: String?): User =
        userApi.updateProfile(UpdateProfileRequestDto(fullName, phone)).toDomain()
}
