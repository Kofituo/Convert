@file:Suppress("NOTHING_TO_INLINE")

package com.otuolabs.unitconverter.functions

import android.util.SparseIntArray
import com.otuolabs.unitconverter.Utils.insertCommas
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.otuolabs.unitconverter.constants.Prefixes
import com.otuolabs.unitconverter.subclasses.Positions
import java.math.BigDecimal

abstract class ConstantsAbstractClass {

    protected var top: Int = 0x00
        set(value) {
            if (field != value) field = value
        }
    protected var bottom = 0x000
        set(value) {
            if (field != value) field = value
        }

    protected var ratio: BigDecimal = BigDecimal.ONE

    protected var fixedValue: BigDecimal = BigDecimal.ZERO
        set(value) {
            if (field != value) field = value
        }

    /**
     * basicFunction(swapConversions())
     * */
    protected inline val result get() = basicFunction(swapConversions())

    /**
     * Throws an exception
     * */
    @Suppress("FunctionName")
    protected inline fun TODO(): Nothing =
        TODO("class: ${this::class.simpleName} top position is $topPosition bottom position is $bottomPosition")

    /**
     * [topPosition] == [int] || [bottomPosition] == [int]
     * */
    protected inline fun intAssertOr(int: Int) = topPosition == int || bottomPosition == int

    /** [topPosition] in [range] && [bottomPosition] in [range]
     * */
    protected inline fun rangeAssertAnd(range: IntRange) =
        topPosition in range && bottomPosition in range


    protected inline fun rangeAssertAnd(range: IntRange, block: () -> Unit) {
        if (rangeAssertAnd(range)) block()
    }

    /**
     * if ([topPosition] == [int] || [bottomPosition] == [int]) [block] ()
     * */
    protected inline fun intAssertOr(int: Int, block: () -> Unit) {
        if (topPosition == int || bottomPosition == int) block()
    }

    /** [topPosition] in [range] || [bottomPosition] in [range]
     * */
    protected inline fun rangeAssertOr(range: IntRange) =
        topPosition in range || bottomPosition in range


    /** [topPosition] in [range] || [bottomPosition] in [range]
     * */
    protected inline fun rangeAssertOr(range: IntRange, block: () -> Unit) {
        if (rangeAssertOr(range)) block()
    }
/*

    protected fun multiplePrefix(sparseIntArray: SparseIntArray) = swapConversions().also {
        innerMultiplePrefix(sparseIntArray)
    }
*/

    protected inline fun innerMultiplePrefix(sparseIntArray: SparseIntArray) {
        sparseIntArray.also {
            //to prevent double calling
            val temp = it[topPosition, -200]
            //which one is not kilogram??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
    }

    protected inline fun amongPrefixes(
        range: IntRange,
        sparseIntArray: () -> SparseIntArray
    ): String? {
        rangeAssertAnd(range) {
            return innerAmongPrefix(sparseIntArray())
        }
        return null
    }

    protected inline fun innerAmongPrefix(sparseIntArray: SparseIntArray): String {
        sparseIntArray.also {
            top = it[topPosition]
            bottom = it[bottomPosition]
        }
        return prefixMultiplication(inputString)
    }

    protected abstract val positions: Positions

    protected inline val topPosition: Int get() = positions.topPosition

    protected inline val bottomPosition: Int get() = positions.bottomPosition

    protected inline val inputString get() = positions.input

    abstract fun getText(): String

    protected fun swapConversions() = if (topPosition > bottomPosition) 1 else -1

    protected fun prefix(): Int =
        Prefixes.prefix(top, bottom)

    private fun basicConversionFunction(x: String, pow: Int): BigDecimal =
        BigDecimal(x).multiply(ratio.pow(pow, mathContext))

    protected fun basicFunction(pow: Int): String =
        basicConversionFunction(inputString, pow).toStringWithCommas()

    protected fun forMultiplePrefixes(pow: Int): String {
        val scale = prefix() * -pow
        return basicConversionFunction(inputString, pow)
            .scaleByPowerOfTen(scale).toStringWithCommas()
    }

    protected fun prefixMultiplication(x: String): String =
        Prefixes.internalPrefixMultiplication(x, prefix())

    /**
     * For use with units such as temperature
     *
     * eg the following is from Delisle to and vice versa::Notice the brackets
     *
     * Celsius	[°C] = 100 − [°De] × ​2 ⁄ 3 ;fixed value is 100 constant is 2/3
     *
     * [°De] = (100 − [°C]) × ​3 ⁄ 2
     *
     * Fahrenheit	[°F] = 212 − [°De] × ​6⁄5
     *
     * [°De] = (212 − [°F]) × ​5⁄6
     *
     * Kelvin    [ K] = 373.15 − [°De] × ​2⁄3
     *
     * [°De] = (373.15 − [ K]) × ​3 ⁄ 2
     *
     * Rankine	[°R] = 671.67 − [°De] × ​6 ⁄ 5
     *
     * [°De] = (671.67 − [°R]) × ​5 ⁄ 6
     *
     *
     * constant is 32 since it does'nt change.
     * use 9 / 5 as ratio
     *  1 (0°C × 9/5) + 32 = F
     *
     *  2 (0°F − 32) × 5/9
     *
     */

    protected fun complexConversionFunction(x: String, pow: Int): String {
        /** for case 1 (0°C × 9/5) + 32 = F
         * x is value to convert
         * the ratio is still the same
         */
        return if (pow == 1) {
            BigDecimal(x)
                .multiply(ratio)
                .add(fixedValue)
                .toStringWithCommas()
        }
        /**
         * for second case 2 (0°F − 32) × 5/9
         * ratio is inverse of itself
         */
        else {
            BigDecimal(x).subtract(fixedValue)
                .multiply(ratio.pow(-1, mathContext))
                .toStringWithCommas()
        }
    }

    //
    protected fun complexConversionFunction(bigDecimal: BigDecimal, pow: Int): String {
        // same as previous function but uses bigDecimal directly
        return if (pow == 1) {
            bigDecimal
                .multiply(ratio)
                .add(fixedValue)
                .toStringWithCommas()
        } //for now else is'nt used
        else {
            bigDecimal
                .subtract(fixedValue)
                .multiply(ratio.pow(-1, mathContext))
                .toStringWithCommas()
        }
    }

    protected fun BigDecimal.toStringWithCommas(): String = stripTrailingZeros().insertCommas()
}