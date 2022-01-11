package com.taxi.app.data.repository

import androidx.lifecycle.LiveData
import com.taxi.app.data.model.api.LogoutRequest
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

    suspend fun logout(logoutRequest: LogoutRequest): LiveData<ApiResult<LogoutResponse>> {
        return performNetworkOperation(networkCall = {
            apiRequest {
                apiSource.logout(logoutRequestBody = logoutRequest)
            }
        })
    }
}