package com.taxi.app.utils

import android.util.Log

object Logger {
    val isLog = false

    fun i(tag: String?, string: String?) {
        if (isLog) {
            Log.i(tag, string!!)
        }
    }

    fun e(tag: String?, string: String?) {
        if (isLog) {
            Log.e(tag, string!!)
        }
    }

    fun d(tag: String?, string: String?) {
        if (isLog) {
            Log.d(tag, string!!)
        }
    }

    fun v(tag: String?, string: String?) {
        if (isLog) {
            Log.v(tag, string!!)
        }
    }

    fun w(tag: String?, string: String?) {
        if (isLog) {
            Log.w(tag, string!!)
        }
    }
}