@file:Suppress("NOTHING_TO_INLINE")

package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.DataStorage
import com.example.unitconverter.subclasses.Positions

class DataStorage(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText() =
        amongBit() ?: bitsConversions() ?: amongByte() ?: ""

    var firstInFocus = false

    private fun amongBit(): String? {
        if (assertAndWideRange(0, 27..34))
            amongBit {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }


    private fun amongByte(): String? {
        if (assertAndWideRange(2, 19..26))
            amongByte {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun bitsPrefix(): Int {
        //to prevent double calling
        amongBit {
            val temp = it[topPosition, -200]
            //which one is not kilogram??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (firstInFocus) -1 else 1
    }

    private fun bitsConversions(): String? {
        if (assertOrWideRange(0, 27..34))
            data {
                ratio = when {
                    assertOrInt(1) -> {
                        //to nibble
                        bitsToNibble
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(bitsPrefix())
            }
        return null
    }

    /** Helper function
     *
     * ([topPosition]== [int] || [topPosition] in [range]) [&&] ([bottomPosition] == [int] [||] [bottomPosition] in [range])
     * */
    private inline fun assertAndWideRange(int: Int, range: IntRange) =
        (topPosition == int || topPosition in range) && (bottomPosition == int || bottomPosition in range)

    /**
     * [topPosition] == [int] || [bottomPosition] == [int] || [topPosition] in [range] || [bottomPosition] in [range]
     *
     * */
    private inline fun assertOrWideRange(int: Int, range: IntRange) =
        assertOrInt(int) || assertOrRange(range)

    private inline fun amongBit(action: (SparseIntArray) -> Unit) =
        DataStorage.amongBits().also(action)

    private inline fun amongByte(action: (SparseIntArray) -> Unit) =
        DataStorage.amongBytes().also(action)

    private inline fun data(block: DataStorage.() -> Unit) = DataStorage.apply(block)
}