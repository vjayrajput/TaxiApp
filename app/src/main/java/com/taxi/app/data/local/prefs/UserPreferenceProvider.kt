package com.taxi.app.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceProvider @Inject constructor(@ApplicationContext context: Context) {

    private val TAG: String = UserPreferenceProvider::class.java.simpleName

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() : SharedPreferences {
            lateinit var preference: SharedPreferences
            try {
                val masterKey = MasterKey.Builder(appContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                preference = EncryptedSharedPreferences.create(
                    appContext,
                    USER_PREFERENCES,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
                preference = appContext.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
            } catch (e: IOException) {
                e.printStackTrace()
                preference = appContext.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
            }
            return preference
        }

    companion object {
        private const val USER_PREFERENCES = "user_shared_preference"
        private const val USER_PREF_KEY_IS_LOGGED_IN = "user_pref_key_is_logged_in"
        private const val USER_PREF_KEY_IS_ACCOUNT_CREATED = "user_pref_key_is_account_created"
        private const val USER_PREF_KEY_USER_ID = "user_pref_key_user_id"
        private const val USER_PREF_KEY_USER_NAME = "user_pref_key_user_name"
        private const val USER_PREF_KEY_USER_PHONE = "user_pref_key_user_phone"
    }

    private fun getPrefString(key: String): String {
        return getPrefString(key, "")
    }

    private fun getPrefString(key: String, default: String): String {
        val data: String? = preference.getString(key, default)
        if (!data.isNullOrEmpty()) {
            return data
        }
        return ""
    }

    var isLoggedIn: Boolean
        get() = preference.getBoolean(USER_PREF_KEY_IS_LOGGED_IN, false)
        set(value) {
            preference.edit().putBoolean(USER_PREF_KEY_IS_LOGGED_IN, value).apply()
        }

    var isAccountCreated: Boolean
        get() = preference.getBoolean(USER_PREF_KEY_IS_ACCOUNT_CREATED, false)
        set(value) {
            preference.edit().putBoolean(USER_PREF_KEY_IS_ACCOUNT_CREATED, value).apply()
        }


    var userId: String
        get() = getPrefString(USER_PREF_KEY_USER_ID, "")
        set(value) {
            preference.edit().putString(USER_PREF_KEY_USER_ID, value).apply()
        }

    var userName: String
        get() = getPrefString(USER_PREF_KEY_USER_NAME, "")
        set(value) {
            preference.edit().putString(USER_PREF_KEY_USER_NAME, value).apply()
        }

    var userPhone: String
        get() = getPrefString(USER_PREF_KEY_USER_PHONE, "")
        set(value) {
            preference.edit().putString(USER_PREF_KEY_USER_PHONE, value).apply()
        }

    fun clearUserSharedPreference() {
        try {
            preference.edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}