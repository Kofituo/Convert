package com.example.unitconverter.miscellaneous

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