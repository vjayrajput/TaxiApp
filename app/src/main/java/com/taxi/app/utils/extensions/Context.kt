package com.taxi.app.utils.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.io.File


fun Context.getAppVersionName(): String {
    try {
        val pInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        return pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

fun Context.getAppVersionCode(): String {
    try {
        val pInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        return pInfo.versionCode.toString()
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

fun Context.getTempFolder(): File {
    val mediaDir =
        File(
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath,
            "temp_folder"
        ).apply { mkdirs() }

    if (!mediaDir.exists()) {
        mediaDir.mkdir()
    }
    return mediaDir
}

fun Context.deleteTempFolderFiles() {
    try {
        val tempFolder = getTempFolder()
        if (tempFolder.exists() && tempFolder.isDirectory) {
            val filesList = tempFolder.listFiles()!!
            for (file in filesList) {
                if (!file.isDirectory) {
                    file.delete()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}


fun Context.getMediaDuration(file: File): Long {
    if (!file.exists()) return 0
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this, Uri.parse(file.absolutePath))
    val duration = retriever.extractMetadata(METADATA_KEY_DURATION)
    retriever.release()
    if (duration != null) {
        return duration.toLongOrNull() ?: 0
    }
    return 0
}

fun Context.getVideoDuration(uri: Uri?): Long {
    var duration = 0L
    try {
        val context = this
        val cursor = MediaStore.Video.query(
            context.contentResolver,
            uri,
            arrayOf(MediaStore.Video.VideoColumns.DURATION)
        )
        if (cursor != null) {
            cursor.moveToFirst()
            duration =
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
            cursor.close()
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return duration
}

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

val Context.versionCode: Long?
    get() = try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo?.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pInfo?.versionCode?.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }

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

fun Context.getStatusBarHeight(): Int {
    var result = 0
    try {
        val resourceId: Int = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun Context.openPlayStoreLink() {
    val appPackageName = this.packageName
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
    } catch (e: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun AppCompatActivity.callTo(phoneNumber: String, requestCode: Int) {
    val intent = Intent(Intent.ACTION_CALL)

    intent.data = Uri.parse("tel:$phoneNumber")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOfNulls<String>(1)
            permissions[0] = Manifest.permission.CALL_PHONE
            requestPermissions(permissions, requestCode)
        } else {
            startActivity(intent)
        }
    } else {
        startActivity(intent)
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


fun Context.getVersionName(): String {
    return try {
        val packageInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

fun Context.getVersionNumber(): Int {
    var versionCode = -1
    try {
        val packageInfo: PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        versionCode = packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versionCode
}

fun Context.getLoopAppDirectory(): File? {
    val directory = File(this.filesDir.toString() + File.separator + "loop")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    return directory
}

fun Context.getWindowSizeByCode(): Int {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display = wm.defaultDisplay
    val outPoint = Point()
    // include navigation bar
    display.getRealSize(outPoint)
    var mRealSizeWidth = 0
    var mRealSizeHeight = 0
    if (outPoint.y > outPoint.x) {
        mRealSizeWidth = outPoint.x
        mRealSizeHeight = outPoint.y
    } else {
        mRealSizeWidth = outPoint.y
        mRealSizeHeight = outPoint.x
    }
    return mRealSizeHeight
}