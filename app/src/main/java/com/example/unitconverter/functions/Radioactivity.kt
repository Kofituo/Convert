package com.example.unitconverter.functions

import com.example.unitconverter.constants.Radioactivity.amongBecquerel
import com.example.unitconverter.constants.Radioactivity.amongCurie
import com.example.unitconverter.constants.Radioactivity.curieToBec
import com.example.unitconverter.subclasses.Positions

class Radioactivity(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongBecquerelAndRuth() ?: amongCurie() ?: becConversions() ?: TODO()

    private fun amongBecquerelAndRuth(): String? {
        if (
            rangeAssertAnd(0..12) //among becquerel
            || rangeAssertAnd(20..26) // among rutherford
            || rangeAssertOr(0..12) && rangeAssertOr(20..26)
        ) amongBecquerel { return innerAmongPrefix(it) }
        return null
    }

    private fun amongCurie(): String? {
        rangeAssertAnd(13..19) {
            amongCurie {
                return innerAmongPrefix(it)
            }
        }
        return null
    }

    private val becPrefixes = {
        amongBecquerel {
            innerMultiplePrefix(it)
        }
        swapConversions()
    }

    private val curiePrefixes = {
        amongCurie {
            innerMultiplePrefix(it)
        }
        swapConversions()
    }

    private fun becConversions(): String? {
        val isBecquerel = rangeAssertOr(0..12)
        if (isBecquerel || rangeAssertOr(20..26)) {
            rangeAssertOr(13..19) {
                ratio = curieToBec
                val pow = addPowers(curiePrefixes, becPrefixes)
                return forMultiplePrefixes(if (isBecquerel) pow else -pow)
            }
        }
        return null
    }

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