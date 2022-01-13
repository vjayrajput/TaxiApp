package com.taxi.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.taxi.app.data.model.api.LoginRequest
import com.taxi.app.data.model.api.LoginResponse
import com.taxi.app.data.model.api.LogoutResponse
import com.taxi.app.data.remote.ApiResult
import com.taxi.app.data.remote.ApiSource
import com.taxi.app.data.remote.SafeApiRequest
import com.taxi.app.data.remote.performNetworkOperation
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiSource: ApiSource) : SafeApiRequest() {

    companion object {
        private val TAG: String = UserRepository::class.java.simpleName
    }

    suspend fun login(loginRequest: LoginRequest): LiveData<ApiResult<LoginResponse>> {
        Log.d(TAG, "login")
        return performNetworkOperation(networkCall = {
            apiRequest {
                apiSource.login(loginRequest = loginRequest)
            }
        })
    }

    suspend fun logout(): LiveData<ApiResult<LogoutResponse>> {
        Log.d(TAG, "logout")
        return performNetworkOperation(networkCall = {
            apiRequest {
                apiSource.logout()
            }
        })
    }
}