package com.example.unitconverter

import android.content.Context
import android.content.SharedPreferences
import android.text.InputFilter
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.example.unitconverter.miscellaneous.isNotNull
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

object Utils {

    var app_bar_bottom = 0

    val minusSign
        get() =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols.minusSign

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

    /*fun Int.toDp(context: Context): Int {
        return this.div((context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT)
    }*/
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
                applyPattern("#,##0.######")
            }
        return if (this is String) decimalFormat.format(this.toBigDecimal())
        else decimalFormat.format(this)
    }

    //filters
    fun filters(comma: Char, fullStop: Char, editText: TextInputEditText): Array<InputFilter> {
        val filter = InputFilter { source, start, end, dest, _, _ ->
            val stringBuilder = StringBuilder(end - start)
            var count = 0
            for (i in start until end) {
                if (source[i].isDigit() ||
                    source[i] == comma ||
                    source[i] == fullStop
                ) {
                    if (source[i] == fullStop) {
                        // ensures only one decimal separator is in the text
                        count++
                        if (count >= 2) continue
                        if (source.length > 1 && editText.isFocused) {
                            val text = editText.text
                            if (text.isNotNull()
                                && text.contains(fullStop)
                            ) continue
                        }
                    }
                    stringBuilder.append(source[i])
                }
            }
            stringBuilder
        }
        //val k = InputFilter { source, start, end, dest, dstart, dend ->  }
        return arrayOf(filter, lengthFilter())
    }

    // temperature filter
    fun temperatureFilters(
        comma: Char,
        fullStop: Char,
        editText: TextInputEditText
    ): Array<InputFilter> {
        val filter = InputFilter { source, start, end, _, _, _ ->
            val stringBuilder = StringBuilder(end - start)
            var count = 0
            for (i in start until end) {
                if (editText.isFocused) Log.e(
                    "i",
                    "i $i source[i] ${source[i]}  source $source  source.lenght ${source.length} edittext last index ${editText.text?.lastIndexOf(
                        minusSign
                    )} souce last index ${source.lastIndexOf(minusSign)}  ${editText.selectionEnd}  ${editText.text?.indexOf(
                        minusSign
                    )}"
                )
                if (source[i] == minusSign) {
                    if (editText.isFocused) Log.e(
                        "if",
                        "first ${i != 0} second ${editText.selectionStart != 0} third ${editText.text?.indexOf(
                            minusSign
                        ) != -1}"
                    )
                    if (i != 0) continue
                    val editTextSelectionStart = editText.selectionStart
                    if (
                        editTextSelectionStart != editText.selectionEnd ||
                        editTextSelectionStart != 0
                    ) continue

                    val text = editText.text
                    if (text.isNotNull() && text.indexOf(minusSign) != -1) continue
                    stringBuilder.append(source[i])
                    if (editText.isFocused) Log.e("ap", "appended")
                }
                if (source[i].isDigit() ||
                    source[i] == comma ||
                    source[i] == fullStop
                ) {
                    if (source[i] == fullStop) {
                        // ensures only one decimal separator is in the text
                        count++
                        if (count >= 2) continue
                        if (source.length > 1 && editText.isFocused) {
                            val text = editText.text
                            if (text.isNotNull()
                                && text.contains(fullStop)
                            ) continue
                        }
                    }
                    stringBuilder.append(source[i])
                }
            }
            if (editText.isFocused) Log.e("string", "$stringBuilder ${editText.text}  $source")
            stringBuilder
        }
        return arrayOf(filter, lengthFilter())
    }

    fun lengthFilter() = InputFilter.LengthFilter(77)//for atm
}
