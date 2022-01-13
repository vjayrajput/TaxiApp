package com.taxi.app.ui.login

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.taxi.app.R
import com.taxi.app.data.model.api.LoginRequest
import com.taxi.app.data.model.api.LoginResponse
import com.taxi.app.data.remote.ApiResult
import com.taxi.app.data.repository.UserRepository
import com.taxi.app.utils.Event
import com.taxi.app.utils.extensions.default
import com.taxi.app.utils.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val userRepository: UserRepository
) :
    ViewModel() {

    companion object {
        private val TAG: String = LoginViewModel::class.java.simpleName
    }

    var email = MutableLiveData<String>().default("")
    var password = MutableLiveData<String>().default("")

    var errorEmail = MutableLiveData<String?>().default(null)
    var errorPassword = MutableLiveData<String?>().default(null)

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _message

    private val _loginResponse = MediatorLiveData<Event<ApiResult<LoginResponse>>>()
    val loginResponse: LiveData<Event<ApiResult<LoginResponse>>>
        get() = _loginResponse

    private fun isValidData(): Boolean {
        var isValid = true
        clearErrorView()
        if (TextUtils.isEmpty(email.value)) {
            errorEmail.value = appContext.getString(R.string.empty_email_validation)
            isValid = false
        }
        if (!TextUtils.isEmpty(email.value) && !email.value!!.isValidEmail()) {
            errorEmail.value = appContext.getString(R.string.valid_email_message)
            isValid = false
        }
        if (TextUtils.isEmpty(password.value!!)) {
            errorPassword.value = appContext.getString(R.string.empty_password_validation)
            isValid = false
        }
        return isValid
    }

    private fun clearErrorView() {
        errorEmail.value = null
        errorPassword.value = null
    }

    fun userLogin() {
        Log.d(TAG, "userLogin")
        if (isValidData()) {
            clearErrorView()
            viewModelScope.launch {
                val request = LoginRequest()
                request.email = email.value!!
                request.password = password.value!!
                _loginResponse.addSource(
                    userRepository.login(request)
                ) {
                    _loginResponse.value = Event(it)
                }
            }
        }
    }

}