package com.taxi.app.utils.extensions

import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.taxi.app.utils.Event
import com.taxi.app.utils.SafeClickListener

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


fun EditText.getValue(): String {
    return this.text.toString().trim()
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}