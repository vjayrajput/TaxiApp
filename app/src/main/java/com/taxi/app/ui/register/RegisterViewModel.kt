package com.taxi.app.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(@ApplicationContext private val appContext: Context) :
    ViewModel() {

    companion object {
        private val TAG: String = RegisterViewModel::class.java.simpleName
    }
}