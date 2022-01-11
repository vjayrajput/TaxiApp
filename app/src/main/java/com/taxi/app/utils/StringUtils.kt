package com.taxi.app.utils

import android.os.Build
import android.text.TextUtils
import java.util.*

const val tagStringUtil = "StringUtils"

val deviceName: String
    get() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) model.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        else manufacturer.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + " " + model
    }

val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.isAlphanumeric: Boolean
    get() = matches(Regex("[a-zA-Z0-9]*"))


fun String.getValue(): String {
    if (TextUtils.isEmpty(this)) {
        return ""
    }

    if (this.lowercase(Locale.getDefault()) == "null") {
        return ""
    }
    return this
}