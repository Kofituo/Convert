package com.example.unitconverter.miscellaneous

import android.content.Context
import android.util.Log
import com.example.unitconverter.R
import com.example.unitconverter.Utils
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormatSymbols

class DecimalFormatFactory {

    private val decimalFormatSymbols = DecimalFormatSymbols()

    data class SymbolAndPattern(
        val decimalFormatSymbols: DecimalFormatSymbols,
        val pattern: String?
    )

    private var pattern: String? = null
    fun buildDecimalFormatSymbols(
        context: Context,
        radioIds: Collection<Int>
    ): SymbolAndPattern {
        Utils.isEngineering = false
        for (ids in radioIds) {
            when (ids) {
                0 -> pattern = "#,##0"

                1 -> pattern = "0E0"

                2 -> {
                    //is engineering
                    Utils.isEngineering = true
                    Log.e("here", "here")
                    pattern = "#00.###############E0"
                }
                4 -> decimalFormatSymbols.groupingSeparator = ','

                5 -> decimalFormatSymbols.groupingSeparator = '.'

                6 -> decimalFormatSymbols.groupingSeparator = ' '

                8 -> decimalFormatSymbols.decimalSeparator = ','

                9 -> decimalFormatSymbols.decimalSeparator = '.'

                10 -> decimalFormatSymbols.decimalSeparator = ' '

                12 -> decimalFormatSymbols.exponentSeparator =
                    context.getString(R.string.small_ten)
            }
        }
        return SymbolAndPattern(
            decimalFormatSymbols,
            pattern
        )
    }

    fun setDecimalPlaces(pattern: String, numberOfPlaces: Int): String {
        val capacity = pattern.length + numberOfPlaces
        return when (pattern) {
            "#,##0" -> buildString(capacity) {
                append(pattern)
                if (numberOfPlaces != 0) append('.')
                for (i in 0 until numberOfPlaces)
                    append('#')
            }
            "0E0" -> buildString(capacity) {
                append('0')
                if (numberOfPlaces != 0) append('.')
                for (i in 0 until numberOfPlaces)
                    append('#')
            }
            else -> TODO()
        }
    }

    fun formatEngineeringString(string: String, numberOfPlaces: Int): String {
        string.apply {
            val stringBeforePoint = substringBefore(Utils.decimalSeparator!!)
            val stringAfterPoint = substringAfter(Utils.decimalSeparator!!)
            val stringForPoint =
                stringAfterPoint.substringBefore('E')
                    .run {
                        val isOverflow = numberOfPlaces + 1 > length
                        val subString =
                            slice(0 until if (isOverflow) length else numberOfPlaces + 1)
                        if (!isOverflow)
                        //for rounding numbers
                            BigDecimal(subString).round(MathContext(subString.trimStart(Utils.decimalSeparator!!).length - 1))
                                .toPlainString().run {
                                    val str = StringBuilder()
                                    //big decimal removes leading zeroes
                                    (0 until subString.length - length).forEach { _ ->
                                        str.append(DecimalFormatSymbols().zeroDigit)
                                    }
                                    if (str.length < numberOfPlaces)
                                        str.append(subSequence(0 until if (length == 1) length else length - 1))
                                    str
                                }
                        else subString
                    }
            val stringFromEToEnd =
                slice(indexOf(Utils.decimalFormatSymbols?.exponentSeparator!!) until length)
            return buildString {
                append(stringBeforePoint)
                if (numberOfPlaces != 0) append(Utils.decimalSeparator!!)
                append(stringForPoint)
                assert((stringForPoint as CharSequence).length <= numberOfPlaces)
                append(stringFromEToEnd)
            }
        }
    }
}
