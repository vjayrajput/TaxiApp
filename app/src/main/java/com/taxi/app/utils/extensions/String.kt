package com.taxi.app.utils.extensions

import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import java.io.File
import java.net.URLConnection
import java.text.DecimalFormat
import java.util.*


const val tagStringUtil = "StringUtils"

fun String?.isImageFile(): Boolean {
    //val mimeType = URLConnection.guessContentTypeFromName(this)
    val mimeType = this
    if (mimeType != null) {
        return mimeType.startsWith("image")
    }
    return false
}

fun String?.isVideoFile(): Boolean {
    val mimeType = this
    if (mimeType != null) {
        return mimeType.startsWith("video")
    }
    return false
}

fun String?.isVideoUrl(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(this)
    if (mimeType != null) {
        return mimeType.startsWith("video")
    }
    return false
}

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

fun String.isValidEmail(): Boolean {
    return if (!TextUtils.isEmpty(this)) {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    } else false
}

fun String.toPriceAmount(): String {
    //println("11".toPriceAmount())
    //println("05".toPriceAmount())
    //11.00
    //5.00
    val dec = DecimalFormat("###,###,###.00")
    return dec.format(this.toDouble())
}

fun Double.toPriceAmount(): String {
    //println(12.0.toPriceAmount())
    //12.00
    val dec = DecimalFormat("###,###,###.00")
    return dec.format(this)
}

fun Double.toCurrencyAmount(): String {
    val dec = DecimalFormat("##,##,##0.##")
    return "₹ ${dec.format(this)}"
}

fun String.toCurrencyAmount(): String {
    val amount = this.toDouble()
    val dec = DecimalFormat("##,##,##0.##")
    return "₹ ${dec.format(amount)}"
}

fun String.getInitials(): String {
    val nameWithoutDoubleSpace = this.replace("\\s+".toRegex(), " ")
    val subNames = nameWithoutDoubleSpace.split("\\s".toRegex())
    return when {
        subNames.size >= 2 -> {
            "${subNames[0][0]}${subNames[1][0]}".uppercase()
        }
        subNames.size == 1 -> {
            "${subNames[0][0]}".uppercase()
        }
        else -> ""
    }
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

fun String.deleteFile() {
    val file = File(this)
    try {
        if (file != null && file.exists()) {
            file.delete()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}