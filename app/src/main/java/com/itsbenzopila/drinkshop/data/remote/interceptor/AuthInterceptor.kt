package com.itsbenzopila.drinkshop.data.remote.interceptor

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val user = FirebaseAuth.getInstance().currentUser
        val tokenTask = user?.getIdToken(false)
        val token = if (tokenTask != null && tokenTask.isComplete && tokenTask.isSuccessful) {
            tokenTask.result?.token
        } else {
            null
        }

        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        val response = try {
            chain.proceed(authenticatedRequest)
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "Network call failed: ${e.message}")
            throw e
        }

        if (response.code == 401 && user != null) {
            Log.d("AuthInterceptor", "Got 401, refreshing token...")
            response.close() // Закрываем старый ответ

            val newToken = try {
                Tasks.await(user.getIdToken(true), 10, TimeUnit.SECONDS).token
            } catch (e: Exception) {
                Log.e("AuthInterceptor", "Token refresh failed: ${e.message}")
                null
            }

            if (newToken != null) {
                val retryRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                return chain.proceed(retryRequest)
            }
        }

        return response
    }
}