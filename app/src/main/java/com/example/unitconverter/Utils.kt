package com.example.unitconverter

import android.content.Context
import android.text.InputFilter
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import androidx.core.util.forEach
import com.example.unitconverter.miscellaneous.isNeitherNullNorEmpty
import com.example.unitconverter.miscellaneous.isNotNull
import com.example.unitconverter.miscellaneous.isNull
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.round

object Utils {

    var app_bar_bottom = 0

    val minusSign
        get() =
            (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols.minusSign

    val <V>SparseArray<V>.values: MutableList<V>
        get() {
            val mutableList = mutableListOf<V>()
            this.forEach { _, value ->
                mutableList.add(value)
            }
            return mutableList
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

    /**
     * map which holds view ids and view names
     * Used to <br>quickly</br> get name of views instead of using
     * resources.getResourceEntryName(view.id)
     * */
    private val mutableMap = mutableMapOf<Int, String>()
    /**
     * used to get name from id
     * fist checks from a map is its there
     * */
    val View.name: String
        get() =
            if (id == -0x1) "no id"
            else {
                var string = mutableMap[this.id]
                if (string.isNull()) {
                    string = resources.getResourceEntryName(this.id)
                    mutableMap[this.id] = string
                }
                string ?: throw Exception("string is surprisingly null")
            }


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
        val filter = InputFilter { source, start, end, _, _, _ ->
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

                if (source[i] == minusSign) {
                    val text = editText.text

                    if (i != 0 || text.isNeitherNullNorEmpty()) continue

                    val editTextSelectionStart = editText.selectionStart

                    if (
                        editTextSelectionStart != editText.selectionEnd ||
                        editTextSelectionStart != 0
                    ) continue

                    if (text.isNotNull() && text.indexOf(minusSign) != -1) continue
                    stringBuilder.append(source[i])
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
            stringBuilder
        }
        return arrayOf(filter, lengthFilter())
    }

    fun lengthFilter() = InputFilter.LengthFilter(77)//for atm

    fun <K, V> Map<K, V>.toJson(): String {
        if (isEmpty()) return "[]"
        return buildString {
            append("[")
            this@toJson.forEach { (k, v) ->
                append("""{"$k":"$v"}""")
                append(",")
            }
            delete(length - 1, length)// deletes the last comma
            append("]")
        }
    }

    /**
     * Returns are new [MutableMap] where the key / values are reversed.
     * i.e. the last pair becomes the first pair
     * */
    fun <K, V> Map<K, V>.reversed(): MutableMap<K, V> {
        if (size < 2) return toMutableMap()
        val reverseValue = values.reversed().iterator()
        val reverseKeys = keys.reversed().iterator()
        val reversedMap = mutableMapOf<K, V>()
        for (i in 0 until size)
            reversedMap[reverseKeys.next()] = reverseValue.next()

        return reversedMap
    }
}
