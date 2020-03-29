@file:Suppress("NOTHING_TO_INLINE", "FunctionWithLambdaExpressionBody")

package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.DataStorage
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal
import kotlin.math.absoluteValue

class DataStorage(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        return amongBit() ?: bitsConversions() ?: amongByte() ?: nibbleConversions()
        ?: bytesConversions()
        ?: amongBiBits() ?: amongBiBytes() ?: biBitConversion()
        ?: TODO()
    }

    /**
     * ReOrdering of the map
     *
     * bits, metric prefixes of bits (kilo,mega,etc), other prefixes (kibi,mebi)
     *
     * nibble,
     *
     * bytes, metric prefixes of bytes (kilo,mega,etc),other prefixes (kibi,mebi)
     * */
    private inline val map get() = lazyMap.value

    lateinit var lazyMap: Lazy<Map<Int, Int>> // lazy initialization

    private fun amongBit(): String? {
        if (wideRangeAssertAnd(0, 27..34))
            amongBit {
                return innerAmongPrefix(it)
            }
        return null
    }

    private fun amongByte(): String? {
        if (wideRangeAssertAnd(2, 19..26))
            amongByte {
                return innerAmongPrefix(it)
            }
        return null
    }

    private fun amongBiBits(): String? = amongBi(3..10)

    private fun amongBi(range: IntRange): String? {
        if (rangeAssertAnd(range))
            biBits(range.first) {
                val first = it[topPosition]
                val second = it[bottomPosition]
                ratio = BigDecimal(2).pow((second - first).absoluteValue)
                return basicFunction(if (first > second) 1 else -1)
            }
        return null
    }

    private fun amongBiBytes(): String? = amongBi(11..18)

    private inline val pow
        get() = map.let {
            if (it.getValue(topPosition) < it.getValue(bottomPosition)) -1 else 1
        }

    private fun bitsPrefix() = {
        amongBit {
            innerMultiplePrefix(it)
        }
        pow
    }

    private fun bytesPrefix() = {
        amongByte {
            innerMultiplePrefix(it)
        }
        pow
    }

    private fun setBinaryRatio(start: Int) {
        biBits(start) {
            val temp = it[topPosition, -200]
            //which one is not binary unit??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            ratio = ratio.multiply(BigDecimal(2).pow(whichOne))
        }
    }

    private fun bitsConversions(): String? {
        if (wideRangeAssertOr(0, 27..34))
            data {
                ratio = when {
                    intAssertOr(1) -> {
                        //to nibble
                        bitsToNibble
                    }
                    wideRangeAssertOr(2, 19..26) -> {
                        // to bytes
                        ratio = bytesToBit
                        return forMultiplePrefixes(-addPowers(bitsPrefix(), bytesPrefix()))
                    }
                    rangeAssertOr(3..10) -> {
                        //to bi bits
                        setBinaryRatio(3)
                        ratio // unchanged
                    }
                    rangeAssertOr(11..18) -> {
                        //to bi bytes
                        ratio = bitsToByte
                        setBinaryRatio(11)
                        return forMultiplePrefixes(bitsPrefix()())
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(bitsPrefix()())
            }
        return null
    }

    private fun nibbleToBytes(): Int {
        amongByte {
            val temp = it[topPosition, -200]
            //which one is not bytes??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = 0
            bottom = whichOne
        }
        return map.let {
            if (it.getValue(topPosition) < it.getValue(bottomPosition)) -1 else 1
        }
    }

    private fun nibbleConversions(): String? {
        if (intAssertOr(1))
            data {
                when {
                    wideRangeAssertOr(2, 19..26) -> {
                        //to bytes
                        ratio = nibbleToByte
                        return forMultiplePrefixes(nibbleToBytes())
                    }
                    rangeAssertOr(3..10) -> {
                        //to bi bits
                        ratio = biBitsToNibble
                        setBinaryRatio(3)
                        return basicFunction(-pow)
                    }
                    rangeAssertOr(11..18) -> {
                        //to bi bytes
                        ratio = nibbleToByte
                        setBinaryRatio(11)
                        return basicFunction(pow)
                    }
                    else -> TODO()
                }
            }
        return null
    }

    private fun bytesConversions(): String? {
        if (wideRangeAssertOr(2, 19..26))
            data {
                return when {
                    rangeAssertOr(3..10) -> {
                        //to bi bits
                        ratio = bytesToBit
                        setBinaryRatio(3)
                        forMultiplePrefixes(-addPowers(bitsPrefix(), bytesPrefix()))
                    }
                    rangeAssertOr(11..18) -> { //wrong
                        //to bi bytes
                        setBinaryRatio(11)
                        forMultiplePrefixes(bytesPrefix()())
                    }
                    else -> TODO()
                }
            }
        return null
    }

    private fun biBitConversion(): String? {
        if (rangeAssertOr(3..10))
            data {
                if (rangeAssertOr(11..18)) {
                    /**
                     * First convert bits then convert from bits bi bytes
                     * */
                    return pow.let {
                        val input = BigDecimal(inputString)
                        //to bits
                        var pow: Int? = null
                        biBits(3) { array: SparseIntArray ->
                            val temp = array[topPosition, -200]
                            //which one is not binary unit??
                            val whichOne =
                                if (temp == -200) array[bottomPosition] else temp
                            pow = whichOne
                        }
                        //from bits to bi bytes
                        biBits(11) { array: SparseIntArray ->
                            val temp = array[topPosition, -200]
                            //which one is not binary unit??
                            val whichOne =
                                if (temp == -200) array[bottomPosition] else temp
                            pow = pow?.minus(whichOne)
                        }
                        if (it < 0)
                            input
                                .multiply(pow?.let { exp ->
                                    BigDecimal(2).pow(exp, mathContext)
                                })
                                .divide(bitsToByte, mathContext)
                                .toStringWithCommas()
                        else
                            input
                                .multiply(pow?.let { exp ->
                                    BigDecimal(2).pow(-exp, mathContext)
                                })
                                .multiply(bitsToByte)
                                .toStringWithCommas()
                    }
                }
            }
        return null
    }

    /** Helper function
     *
     * ([topPosition]== [int] || [topPosition] in [range]) [&&] ([bottomPosition] == [int] || [bottomPosition] in [range])
     * */
    private inline fun wideRangeAssertAnd(int: Int, range: IntRange) =
        (topPosition == int || topPosition in range) && (bottomPosition == int || bottomPosition in range)

    /**
     * [topPosition] == [int] || [bottomPosition] == [int] || [topPosition] in [range] || [bottomPosition] in [range]
     *
     * */
    private inline fun wideRangeAssertOr(int: Int, range: IntRange) =
        intAssertOr(int) || rangeAssertOr(range)

    private inline fun amongBit(action: (SparseIntArray) -> Unit) =
        DataStorage.amongPrefixes(0, 27).also(action)

    private inline fun amongByte(action: (SparseIntArray) -> Unit) =
        DataStorage.amongPrefixes(2, 19).also(action)

    private inline fun biBits(start: Int, action: (SparseIntArray) -> Unit) =
        DataStorage.biBits(start).run(action)

    private inline fun data(block: DataStorage.() -> Unit) = DataStorage.apply(block)

    private inline fun addPowers(first: () -> Int, second: () -> Int): Int {
        first()
        val tempTop = top
        val tempBottom = bottom
        val pow = second()
        top -= tempTop
        bottom -= tempBottom
        return pow
    }
}