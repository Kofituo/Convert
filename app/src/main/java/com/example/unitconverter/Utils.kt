package com.example.unitconverter

import android.content.Context
import android.content.res.Resources
import android.text.InputFilter
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import androidx.core.util.forEach
import com.example.unitconverter.builders.buildMutableList
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.miscellaneous.isNeitherNullNorEmpty
import com.example.unitconverter.miscellaneous.isNotNull
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
        get() =
            buildMutableList(30) {
                this@values.forEach { _, value ->
                    add(value)
                }
            }

    fun Int.dpToInt(context: Context): Int = round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            context.resources.displayMetrics
        )
    ).toInt()

    /*fun Int.toDp(context: Context): Int {
        return this.div((context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT)
    }*/

    /**
     * A map which holds view ids and view names
     * Used to <br>quickly</br> get name of views instead of using
     * resources.getResourceEntryName(view.id)
     * */
    private val mutableMap = mutableMapOf<Int, String>()

    /**
     * Used to get name from id
     * First checks from a map is its there //for fast access
     * */
    val View.name: String
        get() =
            if (id == -0x1) throw Resources.NotFoundException("Invalid ID '-1' $this")
            else mutableMap[this.id]
                ?: resources.getResourceEntryName(this.id) //returns a string
                    .apply { mutableMap[this@name.id] = this } //updates the map

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
                val text = editText.text
                val editTextSelectionStart = editText.selectionStart
                if (text.isNotNull() &&
                    text.indexOf(minusSign) == editTextSelectionStart
                ) continue // prevent things like 4-5.0

                if (source[i] == minusSign) {
                    if (i != 0 || text.isNeitherNullNorEmpty()) continue
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
        return buildMutableMap {
            for (i in 0 until size)
                this@buildMutableMap[reverseKeys.next()] = reverseValue.next()
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun StringBuilder.appendWithSpace(string: String): StringBuilder {
        append(string)
        append(" ")
        return this
    }

    /**
     * Performs the given [action] on each element, providing sequential index with the element.
     * @param [action] function that takes the index of an element and the element itself
     * and performs the desired action on the element.
     */

    inline fun IntRange.forEachIndexed(start: Int, action: (index: Int, Int) -> Unit): Unit {
        var index = start
        for (item in this) action(index++, item)
    }
}