package com.taxi.app.data.remote

import com.taxi.app.data.model.api.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSource {

    @POST("/api/authaccount/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/authaccount/registration")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse


    @POST("logout")
    suspend fun logout(): LogoutResponse

}