package com.taxi.app.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Context.getAppVersionName(): String {
    try {
        val pInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        return pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isActive)
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)


fun Context.toast(message: String?) {
    if (this != null) {
        this.toast(message, Toast.LENGTH_SHORT)
    }
}

fun Context.toastLong(message: String?) {
    if (this != null) {
        this.toast(message, Toast.LENGTH_LONG)
    }
}

fun Context.toast(message: String?, duration: Int) {
    if (this != null && !TextUtils.isEmpty(message) && message!!.isNotEmpty()) {
        try {
            Toast.makeText(this, message, duration).show()
        } catch (e: WindowManager.BadTokenException) {

        } catch (e: Exception) {

        }
    }
}

fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.let {
        it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }
    return result
}