package com.taxi.app.ui.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.utils.extensions.default
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val userPref: UserPreferenceProvider
) : ViewModel() {

    companion object {
        private val TAG: String = DashboardViewModel::class.java.simpleName
    }

    var latitude = MutableLiveData<String>().default("0.0")
    var longitude = MutableLiveData<String>().default("0.0")

    var name = MutableLiveData<String>().default("")
    var email = MutableLiveData<String>().default("")
    var phoneNumber = MutableLiveData<String>().default("")
    var gender = MutableLiveData<String>().default("")

    init {
        name.value = userPref.userName
        email.value = userPref.userEmail
        phoneNumber.value = userPref.userPhoneNumber
        gender.value = userPref.userGender
        latitude.value = userPref.latitude
        longitude.value = userPref.longitude
    }
}