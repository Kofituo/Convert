package com.otuolabs.unitconverter.miscellaneous

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.round

class DecimalFormatFactory {

    private val decimalFormatSymbols by lazy(LazyThreadSafetyMode.NONE) {
        DecimalFormatSymbols.getInstance()
    }

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
                    pattern = "#00.###############E0"
                }
                4 -> decimalFormatSymbols.groupingSeparator = ','

                5 -> decimalFormatSymbols.groupingSeparator = '.'

                6 -> decimalFormatSymbols.groupingSeparator = ' '

                8 -> decimalFormatSymbols.decimalSeparator = ','

                9 -> decimalFormatSymbols.decimalSeparator = '.'
                // normal space so if someone changes the local, the default would
                // be non breaking space and this won't cause calculation problems
                10 -> decimalFormatSymbols.decimalSeparator = ' '

                12 -> decimalFormatSymbols.exponentSeparator =
                    context.getString(R.string.small_ten)
            }
        }
        return SymbolAndPattern(decimalFormatSymbols, pattern)
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
                append("E0")
            }
            else -> TODO()
        }
    }

    fun format(decimalFormat: DecimalFormat, bigDecimal: BigDecimal, int: Int): String {
        if (int == 0)
        //when number of decimal places is zero
            return decimalFormat.format(bigDecimal).run {
                Utils.let {
                    val dotIndex = indexOf(it.decimalSeparator)
                    val eIndex = indexOf(it.exponentSeparator)
                    if (dotIndex == -1 || eIndex == -1)
                        return this
                    // throw AssertionError("index is -1 e $eIndex  . $dotIndex  $this")
                    //really inefficient
                    //TODO get a more efficient way to round
                    buildString {
                        append(round(this@run.substring(0..dotIndex + 1).toDouble()).toInt())
                        append(this@run.substring(eIndex until this@run.length))
                    }
                }
            }
        val string = StringBuilder(bigDecimal.abs().toPlainString())
        /**
         * case where the number is less than one
         * */
        Utils.let {
            if (string.startsWith("${it.zero}${it.decimalSeparator}")) {
                //it could be that the number is 0.000, 0.00000
                string.deleteCharAt(1)
                val isZero = string.all { char: Char ->
                    char == it.zero
                }
                if (isZero) {
                    return "${it.zero}${it.exponentSeparator}${it.zero}"
                }
                var zerosCounter = 0
                //deletes the first zero
                for (i in string.deleteCharAt(0)) {
                    if (i != it.zero)
                        break
                    zerosCounter++
                }
                var numbersBeforeDot = zerosCounter % 3
                when (numbersBeforeDot) {
                    0 -> {
                        numbersBeforeDot += 3
                    }
                    1 -> {
                        numbersBeforeDot += 1
                    }
                    else -> numbersBeforeDot -= 1
                }
                val roundNumber = numbersBeforeDot + int
                val newBigDecimal = bigDecimal.round(MathContext(roundNumber))
                return decimalFormat.format(newBigDecimal)
            } else {
                val dotIndex = string.indexOf(it.decimalSeparator)
                if (dotIndex != -1)
                    string.delete(dotIndex, string.length)
                var numbersBeforeDot = string.length % 3
                if (numbersBeforeDot == 0) {
                    numbersBeforeDot += 3
                }
                val roundNumber = numbersBeforeDot + int
                val newBigDecimal = bigDecimal.round(MathContext(roundNumber))
                return decimalFormat.format(newBigDecimal)
            }
        }
    }
}
/*fun formatEngineeringString(string: String, numberOfPlaces: Int): String {
        string.apply {
            val index = indexOf(Utils.decimalSeparator)
            val stringBeforePoint = if (index == -1) return string else substring(0, index)
            val stringAfterPoint = substringAfter(Utils.decimalSeparator)
            val stringForPoint =
                stringAfterPoint.substringBefore(Utils.decimalFormatSymbols!!.exponentSeparator)
                    .run {
                        val isOverflow = numberOfPlaces + 1 > length
                        val subString =
                            slice(0 until if (isOverflow) length else numberOfPlaces + 1)
                        if (!isOverflow)
                        //for rounding numbers
                            BigDecimal(subString)
                                .round(
                                    MathContext(subString.trimStart(Utils.decimalSeparator).length - 1)
                                )
                                .toPlainString().run {
                                    '3'.isDefined()
                                    Log.e(
                                        "sub",
                                        "$subString  ${subString.trimStart(Utils.decimalSeparator)}  $string"
                                    )
                                    val str = StringBuilder()
                                    //big decimal removes leading zeroes
                                    (0 until subString.length - length).forEach { _ ->
                                        str.append(Utils.zero)
                                    }
                                    if (str.length < numberOfPlaces)
                                        str.append(substring(0 until if (length == 1) length else length - 1))
                                    str
                                }
                        else subString
                    }
            val stringFromEToEnd =
                slice(indexOf(Utils.decimalFormatSymbols?.exponentSeparator!!) until length)
            return buildString {
                append(stringBeforePoint)

                if (numberOfPlaces != 0) append(Utils.decimalSeparator)
                put {
                    stringForPoint as CharSequence
                    this value
                            if (stringForPoint.length > numberOfPlaces)
                                stringForPoint.slice(0 until numberOfPlaces)
                            else stringForPoint
                }
                append(stringFromEToEnd)
            }
        }
    }*/