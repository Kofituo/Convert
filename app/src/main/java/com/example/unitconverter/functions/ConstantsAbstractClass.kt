package com.example.unitconverter.functions


import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.constants.ConstantsInterface
import com.example.unitconverter.constants.Prefixes
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormatSymbols

abstract class ConstantsAbstractClass : ConstantsInterface {

    var top: Int = 0x00
        set(value) {
            if (field != value) field = value
        }
    var bottom = 0x000
        set(value) {
            if (field != value) field = value
        }

    var ratio: BigDecimal = BigDecimal.ONE
        set(value) {
            if (field != value) field = value
        }

    var fixedValue: BigDecimal = BigDecimal.ZERO
        set(value) {
            if (field != value) field = value
        }

    abstract val positions: Positions

    val topPosition: Int get() = positions.topPosition

    val bottomPosition: Int get() = positions.bottomPosition

    val inputString get() = positions.input

    abstract fun getText(): String

    fun swapConversions() = if (topPosition > bottomPosition) 1 else -1

    private fun prefix(): Int =
        Prefixes.prefix(top, bottom)

    private fun basicConversionFunction(x: String, pow: Int): BigDecimal =
        BigDecimal(x).multiply((ratio).pow(pow, mathContext))

    fun basicFunction(x: String, pow: Int): String? =
        basicConversionFunction(x, pow).toStringWithCommas()

    fun forMultiplePrefixes(x: String, pow: Int): String? {
        val scale = prefix() * -pow
        return basicConversionFunction(x, pow)
            .scaleByPowerOfTen(scale).toStringWithCommas()
    }

    fun prefixMultiplication(x: String): String =
        Prefixes.internalPrefixMultiplication(x, prefix())

    fun complexConversionFunction(x: String, pow: Int): String {
        /**
         * For use with units such as temperature
         * eg the following is from Delisle to and vice versa::Notice the brackets
         * Celsius	[°C] = 100 − [°De] × ​2 ⁄ 3 ;fixed value is 100 constant is 2/3
         * [°De] = (100 − [°C]) × ​3 ⁄ 2
         * Fahrenheit	[°F] = 212 − [°De] × ​6⁄5
         * [°De] = (212 − [°F]) × ​5⁄6
         * Kelvin    [ K] = 373.15 − [°De] × ​2⁄3
         * [°De] = (373.15 − [ K]) × ​3 ⁄ 2
         * Rankine	[°R] = 671.67 − [°De] × ​6 ⁄ 5
         * [°De] = (671.67 − [°R]) × ​5 ⁄ 6
         *
         * constant is 32 since it does'nt change
         * use 9 / 5 as ratio
         *  1 (0°C × 9/5) + 32 = F
         *
         *  2 (0°F − 32) × 5/9
         *
         */

        /** for case 1 (0°C × 9/5) + 32 = F
         * x is value to convert
         * the ratio is still the same
         */
        return if (pow == 1) {
            BigDecimal(x)
                .quickMultiplication(ratio)
                .add(fixedValue)
                .toStringWithCommas()
        }
        /**
         * for second case 2 (0°F − 32) × 5/9
         * ratio is inverse of itself
         */
        else {
            BigDecimal(x)
                .subtract(fixedValue)
                .quickMultiplication(ratio.pow(-1, mathContext))
                .toStringWithCommas()
        }
    }

    //
    fun complexConversionFunction(bigDecimal: BigDecimal, pow: Int): String {
        // same as previous function but uses bigDecimal directly
        return if (pow == 1) {
            bigDecimal
                .quickMultiplication(ratio)
                .add(fixedValue)
                .toStringWithCommas()
        } //for now else is'nt used
        else {
            bigDecimal
                .subtract(fixedValue)
                .quickMultiplication(ratio.pow(-1, mathContext))
                .toStringWithCommas()
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun BigDecimal.toStringWithCommas(): String =
        if (this.setScale(20, RoundingMode.HALF_EVEN)
                .compareTo(BigDecimal.ZERO) == 0
        ) DecimalFormatSymbols().zeroDigit.toString()
        else this
            .stripTrailingZeros()
            .insertCommas()

    private fun BigDecimal.quickMultiplication(other: BigDecimal) =
        if (other == BigDecimal.ONE) this else this.multiply(other)
}