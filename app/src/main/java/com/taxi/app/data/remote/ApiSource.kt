package com.taxi.app.data.remote

import com.taxi.app.data.model.api.LoginRequest
import com.taxi.app.data.model.api.LoginResponse
import com.taxi.app.data.model.api.LogoutResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSource {

    @POST("/api/authaccount/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("logout")
    suspend fun logout(): LogoutResponse

}