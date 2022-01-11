package com.taxi.app.utils

import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import java.util.*


fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner) { event ->
        event?.getContentIfNotHandled()?.let { observer.onChanged(it) }
    }
}

fun View.visible() {
    if (this != null) {
        visibility = View.VISIBLE
    }
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.gone() {
    if (this != null) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (this != null) {
        visibility = View.INVISIBLE
    }
}

fun View.show(isShow: Boolean) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

fun TextInputLayout.showTilError(message: String) {
    try {
        this.isErrorEnabled = true
        this.error = message
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun TextInputLayout.clearTilError() {
    try {
        this.error = null
        this.isErrorEnabled = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun EditText.getValue(): String {
    return this.text.toString().trim()
}

fun EditText.onAction(action: Int, runAction: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            action -> {
                runAction.invoke()
                true
            }
            else -> false
        }
    }
}