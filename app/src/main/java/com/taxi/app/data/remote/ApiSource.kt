package com.taxi.app.data.remote

import com.taxi.app.data.model.api.LogoutRequest
import com.taxi.app.data.model.api.LogoutResponse
import retrofit2.http.Body
import retrofit2.http.HTTP

interface ApiSource {

    @HTTP(method = "DELETE", path = "users/sign_out", hasBody = true)
    suspend fun logout(@Body logoutRequestBody: LogoutRequest): LogoutResponse

}