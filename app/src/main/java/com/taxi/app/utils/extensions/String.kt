package com.taxi.app.utils.extensions

import android.text.TextUtils
import android.util.Patterns
import java.util.*


const val tagStringUtil = "StringUtils"

fun String.isValidEmail(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    } else false
}

fun String.getValue(): String {
    if (TextUtils.isEmpty(this)) {
        return ""
    }

    if (this.lowercase(Locale.getDefault()) == "null") {
        return ""
    }
    return this
}