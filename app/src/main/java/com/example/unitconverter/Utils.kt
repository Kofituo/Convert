package com.example.unitconverter

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.View
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

object Utils {

    var app_bar_bottom = 0

    fun SharedPreferences.Editor.putIntegerArrayList(
        key: String,
        list: ArrayList<Int>
    ): SharedPreferences.Editor {
        putString(key, list.joinToString(","))
        return this
    }

    fun SharedPreferences.getIntegerArrayList(
        key: String,
        default: ArrayList<Int>
    ): ArrayList<Int> {
        val value = getString(key, null)
        if (value.isNullOrBlank()) return default
        return ArrayList(value.split(",").map { it.toInt() })
    }

    internal fun Int.dpToInt(context: Context): Int = round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        )
    ).toInt()


    // used to get name from id
    val View.name: String
        get() =
            if (this.id == -0x1) "no id"
            else resources.getResourceEntryName(this.id)

    fun String.removeCommas(decimalSeparator: Char): String? {
        if (this.isBlank()) return ""
        val checkString = StringBuilder()
        when {
            this.startsWith(decimalSeparator) ->
                checkString.append(0).append(this)
            this.endsWith(decimalSeparator) ->
                checkString.append(this).append(0)
            else -> checkString.append(this)
        }
        (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
            isParseBigDecimal = true
            return parse(checkString.toString())?.toString()
        }
    }

    fun <T> T.insertCommas(): String {
        val decimalFormat =
            (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
                applyPattern("#,##0.#######")
            }
        return if (this is String) decimalFormat.format(this.toBigDecimal()) else decimalFormat.format(
            this
        )
    }
}