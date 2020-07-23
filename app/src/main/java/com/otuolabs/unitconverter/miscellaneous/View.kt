package com.otuolabs.unitconverter.miscellaneous

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.otuolabs.unitconverter.Utils.dpToInt

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) =
        (layoutParams as T).apply(block)

data class LayoutInflater(
        var resourceId: Int = 0,
        var root: ViewGroup? = null,
        var attachToRoot: Boolean = root != null
)

inline fun android.view.LayoutInflater.inflate(values: LayoutInflater.() -> Unit): View =
        LayoutInflater().apply(values).run {
            inflate(resourceId, root, attachToRoot)
        }

data class Toast(var stringId: Int = -1, var duration: Int = -1, var text: CharSequence? = null)

@SuppressLint("WrongConstant")
inline fun Context.showToast(block: Toast.() -> Unit) {
    Toast().apply(block).run {
        if (stringId == -1) {
            android.widget.Toast.makeText(this@showToast, text, duration).show()
        } else android.widget.Toast.makeText(this@showToast, stringId, duration).show()
    }
}

fun View.setLeftPadding(context: Context, padding: Int) {
    apply {
        setPadding(
                padding.dpToInt(context),
                paddingTop,
                paddingRight,
                paddingBottom
        )
    }
}

fun View.setTopPadding(context: Context, padding: Int) {
    apply {
        setPadding(
                paddingLeft,
                padding.dpToInt(context),
                paddingRight,
                paddingBottom
        )
    }
}

data class ViewData(val id: Int, val name: String, val text: CharSequence, val metadata: CharSequence?) : JsonConvertible {
    override fun toJson() = """["$id" , "$name", "$text", "$metadata"]"""
}