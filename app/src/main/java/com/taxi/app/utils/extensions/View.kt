package com.taxi.app.utils.extensions

import android.content.Context
import android.content.ContextWrapper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
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

/**
 * hide keyboard
 *
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun View.onWindowInsets(action: (View, WindowInsetsCompat) -> Unit) {
    ViewCompat.requestApplyInsets(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        action(v, insets)
        insets
    }
}

fun Window.fitSystemWindows() {
    WindowCompat.setDecorFitsSystemWindows(this, false)
}

//fun Fragment.share(media: Media, title: String = "Share with...") {
//    val share = Intent(Intent.ACTION_SEND)
//    share.type = "image/*"
//    share.putExtra(Intent.EXTRA_STREAM, media.uri)
//    startActivity(Intent.createChooser(share, title))
//}

fun ViewPager2.onPageSelected(action: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            action(position)
        }
    })
}


var View.topMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = value
        this.layoutParams = params
    }

var View.bottomMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = value
        this.layoutParams = params
    }

var View.endMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).marginEnd
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.marginEnd = value
        this.layoutParams = params
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

fun TextInputLayout.setTextChangedListener() {
    try {
        this.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var enable = true
                if (TextUtils.isEmpty(editText?.text.toString().trim { it <= ' ' })) {
                    enable = false
                }
                if (enable) {
                    clearTilError()
                }
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
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

fun View.getParentActivity(): AppCompatActivity? {
    var context: Context? = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return (context as AppCompatActivity?)!!
        }
        context = context.baseContext
    }
    return null
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}