package com.taxi.app.ui.register

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.taxi.app.R
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.data.model.api.RegisterRequest
import com.taxi.app.data.model.api.RegisterResponse
import com.taxi.app.data.remote.ApiResult
import com.taxi.app.data.remote.Status
import com.taxi.app.data.repository.UserRepository
import com.taxi.app.utils.Event
import com.taxi.app.utils.extensions.default
import com.taxi.app.utils.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val userPref: UserPreferenceProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG: String = RegisterViewModel::class.java.simpleName
    }

    var name = MutableLiveData<String>().default("")
    var email = MutableLiveData<String>().default("")
    var password = MutableLiveData<String>().default("")

    var errorName = MutableLiveData<String?>().default(null)
    var errorEmail = MutableLiveData<String?>().default(null)
    var errorPassword = MutableLiveData<String?>().default(null)

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = _message

    private val _registerResponse = MediatorLiveData<Event<ApiResult<RegisterResponse>>>()
    val registerResponse: LiveData<Event<ApiResult<RegisterResponse>>>
        get() = _registerResponse

    private fun isValidData(): Boolean {
        var isValid = true
        clearErrorView()
        if (TextUtils.isEmpty(name.value)) {
            errorName.value = appContext.getString(R.string.empty_name_validation)
            isValid = false
        }
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
        errorName.value = null
        errorEmail.value = null
        errorPassword.value = null
    }

    fun userRegister() {
        Log.d(TAG, "userRegister")
        if (isValidData()) {
            clearErrorView()
            viewModelScope.launch {
                val request = RegisterRequest()
                request.name = name.value!!
                request.email = email.value!!
                request.password = password.value!!
                _registerResponse.addSource(userRepository.register(request)) {
                    if (it.status == Status.SUCCESS) {
                        val code = it.data?.code
                        if (code != 1) {
                            val user = it.data?.userData
                            userPref.userId = user?.id ?: ""
                            userPref.userName = user?.name ?: ""
                            userPref.userEmail = user?.email ?: ""
                            userPref.accessToken = user?.token ?: ""
                            userPref.isLoggedIn = true
                        } else {
                            _message.value = Event(it.data.message)
                        }
                    }
                    _registerResponse.value = Event(it)
                }
            }
        }
    }

}