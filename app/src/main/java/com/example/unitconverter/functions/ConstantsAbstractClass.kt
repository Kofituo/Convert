package com.example.unitconverter.functions


import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.constants.ConstantsInterface
import com.example.unitconverter.constants.Prefixes
import java.math.BigDecimal
import java.math.MathContext

abstract class ConstantsAbstractClass : ConstantsInterface {

    var top: Int = 0x00
        set(value) {
            if (field != value) field = value
        }
    var bottom = 0x000
        set(value) {
            if (field != value) field = value
        }

    var constant: BigDecimal = BigDecimal.ZERO
        set(value) {
            if (field != value) field = value
        }

    abstract val topPosition: Int

    abstract val bottomPosition: Int

    abstract val inputString: String

    abstract fun getText(): String

    fun swapConversions() = if (topPosition > bottomPosition) 1 else -1

    private fun prefix(): Int =
        Prefixes.prefix(top, bottom)

    private fun basicConversionFunction(x: String, pow: Int): BigDecimal =
        BigDecimal(x).multiply((constant).pow(pow, mathContext))

    fun basicFunction(x: String, pow: Int): String? =
        basicConversionFunction(x, pow).stripTrailingZeros().insertCommas()

    fun forMultiplePrefixes(x: String, pow: Int): String? {
        val scale = prefix() * -pow
        return basicConversionFunction(x, pow)
            .scaleByPowerOfTen(scale).stripTrailingZeros().insertCommas()
    }

    fun prefixMultiplication(x: String): String =
        Prefixes.internalPrefixMultiplication(x, prefix())

    fun complexConversionFunction(x: BigDecimal, fixedValueInt: String, pow: Int): String {
        /**
         * For use with units such as temperature
         * eg the following is from Delisle to and vice versa::Notice the brackets
         * Celsius	[°C] = 100 − [°De] × ​2 ⁄ 3 ;fixed value is 100 constant is 2/3
         * [°De] = (100 − [°C]) × ​3 ⁄ 2
         * Fahrenheit	[°F] = 212 − [°De] × ​6⁄5
         * [°De] = (212 − [°F]) × ​5⁄6
         * Kelvin    [K] = 373.15 − [°De] × ​2⁄3
         * [°De] = (373.15 − [K]) × ​3 ⁄ 2
         * Rankine	[°R] = 671.67 − [°De] × ​6 ⁄ 5
         * [°De] = (671.67 − [°R]) × ​5 ⁄ 6
         *
         * conversions aren't uniform
         */
        val newConstant: BigDecimal
        val fixedValue = BigDecimal(fixedValueInt)
        return if (pow == -1) {
            newConstant = BigDecimal.ONE.divide(constant, MathContext(99))
            fixedValue.subtract(x.multiply(newConstant))
                .stripTrailingZeros().insertCommas()

        } else {
            fixedValue.subtract(x).multiply(constant)
                .stripTrailingZeros().insertCommas()
        }
    }
}