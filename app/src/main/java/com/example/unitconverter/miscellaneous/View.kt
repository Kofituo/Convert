package com.example.unitconverter.miscellaneous

import android.app.Activity
import android.view.View
import android.view.ViewGroup

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) =
    (layoutParams as T).apply(block)

data class LayoutInflater(
    var resourceId: Int? = null,
    var root: ViewGroup? = null,
    var attachToRoot: Boolean = root != null
)

inline fun android.view.LayoutInflater.inflate(values: LayoutInflater.() -> Unit): View =
    LayoutInflater().apply(values).run {
        inflate(resourceId!!, root, attachToRoot)
    }

data class Toast(var resId: Int? = null, var duration: Int? = null, var text: CharSequence? = null)

inline fun Activity.showToast(block: Toast.() -> Unit) {
    Toast().apply(block).run {
        if (resId == null) {
            android.widget.Toast.makeText(this@showToast, text, duration!!).show()
        } else android.widget.Toast.makeText(this@showToast, resId!!, duration!!).show()
    }
}