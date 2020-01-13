package com.example.unitconverter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.TypedValue
import android.view.View
import java.math.BigDecimal
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

    fun String.removeCommas(decimalSeparator: String): String? {
        if (this.isBlank()) return ""
        val checkString = StringBuilder()
        when {
            this.startsWith(decimalSeparator) ->
                checkString.append(0).append(this)

            this.endsWith(decimalSeparator) ->
                checkString.append(this).append(0)
            else -> checkString.append(this)
        }
        Log.e("chec", "$checkString")
        val decimalSeparatorIndex = this.indexOf(decimalSeparator)

        if (decimalSeparatorIndex != -1) {
            val temp = checkString.indexOf(decimalSeparator)
            checkString.replace(temp, temp + 1, "")
        }
        (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
            isParseBigDecimal = true
            val initialLength = checkString.length
            val preResult = parse(checkString.toString())//?.toString()
            val result = preResult?.toString()
            val finalLength = result?.length!!
            val diff = initialLength - finalLength
            if (decimalSeparatorIndex != -1 && result[0] != '0') {
                return StringBuilder(result).insert(
                    if (decimalSeparatorIndex - diff < 0) decimalSeparatorIndex else decimalSeparatorIndex - diff,
                    decimalSeparator
                )
                    .toString()
            }
            return result
        }
    }

    fun BigDecimal.insertCommas(): String {
        val decimalFormat =
            (NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat).apply {
                applyLocalizedPattern("#,##0.#######")
            }
        return decimalFormat.format(this)
    }
}