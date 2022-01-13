package com.taxi.app.utils

import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeoutException


const val TAG = "Utils"


fun Throwable?.isNetworkError(): Boolean {
    val throwable = this
    if (throwable != null && (throwable is ConnectException || throwable is UnknownHostException)) {
        //No internet connection found
        return true
    }
    return false
}

fun Throwable?.isSocketTimeout(): Boolean {
    val throwable = this
    if (throwable != null && (throwable is SocketTimeoutException || throwable is TimeoutException)) {
        //socket timeout found
        return true
    }
    return false
}

fun getOsVersion(): String {
    return Build.VERSION.RELEASE
}

fun getDeviceModel(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    if (model.startsWith(manufacturer)) {
        return model.uppercase(Locale.getDefault())
    }
    return manufacturer.uppercase(Locale.getDefault()) + " " + model
}


fun EditText.clearFocusOnActionDone() {
    try {

        this.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Clear focus here from edittext
                this.clearFocus()
            }
            false
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getCurrentDateTime(): String {
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
        return sdf.format(Date())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun getUriFromPath(path: String): Uri {
    if (!TextUtils.isEmpty(path)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Uri.parse(File(path).toString())
        } else {
            return Uri.fromFile(File(path))
        }
    }
    return Uri.EMPTY
}

fun isSdkHigherThan28(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun formatCSeconds(timeInSeconds: Long): String {
    val hours = timeInSeconds / 3600
    val secondsLeft = timeInSeconds - hours * 3600
    val minutes = secondsLeft / 60
    val seconds = secondsLeft - minutes * 60
    var formattedTime = ""
    if (hours < 10) formattedTime += "0"
    formattedTime += "$hours:"
    if (minutes < 10) formattedTime += "0"
    formattedTime += "$minutes:"
    if (seconds < 10) formattedTime += "0"
    formattedTime += seconds
    return formattedTime
}

fun formatSeconds(timeInSeconds: Long): String {
    val hours = timeInSeconds / 3600
    val secondsLeft = timeInSeconds - hours * 3600
    val minutes = secondsLeft / 60
    val seconds = secondsLeft - minutes * 60
    var formattedTime = ""
    if (hours < 10 && hours != 0L) {
        formattedTime += "0"
        formattedTime += "$hours:"
    }
    if (minutes < 10) formattedTime += "0"
    formattedTime += "$minutes:"
    if (seconds < 10) formattedTime += "0"
    formattedTime += seconds
    return formattedTime
}

