package com.example.unitconverter.miscellaneous

import android.view.View
import android.view.ViewGroup

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) =
    (layoutParams as T).apply(block)