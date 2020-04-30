package com.example.unitconverter.functions

import com.example.unitconverter.constants.Angle
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Velocity
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Velocity(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongRad() ?: radiansPerSecConversion() ?: amongDegrees() ?: degreesPerSecConversions()
        ?: amongGrad() ?: gradiansConversion() ?: amongRev()
        ?: TODO()

    private fun amongTimes(intRange: IntRange): String =
        // use time class to get result quickly
        intRange.iterator().run {
            val map = mapOf(
                nextInt() to 0,
                nextInt() to 17,
                nextInt() to 18,
                nextInt() to 19
            )
            return Time(
                Positions(map.getValue(bottomPosition), map.getValue(topPosition), inputString)
            ).getText()
        }

    private fun getExp(intRange: IntRange): Int {
        val map = Velocity.amongSeconds(intRange)
        val first = map[topPosition, 200]
        if (first != 200) return first
        val second = map[bottomPosition, 200]
        if (second != 200) return second
        TODO()
    }

    private inline fun amongUnits(intRange: () -> IntRange) =
        if (rangeAssertAnd(intRange())) amongTimes(intRange()) else null

    private fun amongRad() = amongUnits { 0..3 }

    private fun amongDegrees() = amongUnits { 4..7 }

    private fun amongGrad() = amongUnits { 8..11 }

    private fun amongRev() = amongUnits { 12..15 }

    private fun radiansPerSecConversion(): String? {
        rangeAssertOr(0..3) {
            when {
                rangeAssertOr(4..7) -> {
                    //to degrees
                    return getConversion(0..3, 4..7) {
                        Angle.degreesToRadians
                    }
                }
                rangeAssertOr(8..11) -> {
                    //to gradians
                    return getConversion(0..3, 8..11) {
                        inverseOf(Angle.radiansToGradians)
                    }
                }
                rangeAssertOr(12..15) -> {
                    //to revolution
                    return getConversion(0..3, 12..15) {
                        inverseOf(Angle.radiansToRevolutions)
                    }
                }
                else -> TODO()
            }
        }
        return null
    }

    private fun degreesPerSecConversions(): String? {
        rangeAssertOr(4..7) {
            return when {
                rangeAssertOr(8..11) -> {
                    //to gradians
                    getConversion(4..7, 8..11) {
                        inverseOf(Angle.degreesToGradians)
                    }
                }
                rangeAssertOr(12..15) -> {
                    //to revolution
                    getConversion(4..7, 12..15) {
                        inverseOf(Angle.degreesToRevolution)
                    }
                }
                else -> TODO()
            }
        }
        return null
    }

    private fun gradiansConversion(): String? {
        rangeAssertOr(8..11) {
            rangeAssertOr(12..15) {
                //to revolution
                return getConversion(8..11, 12..15) {
                    inverseOf(Angle.gradiansToRevolutions)
                }
            }
        }
        return null
    }

    private inline fun getConversion(
        firstRange: IntRange,
        secondRange: IntRange,
        bigDecimal: () -> BigDecimal
    ): String {
        rangeAssertOr(IntRange(secondRange.first, secondRange.last - 1)) {
            val firstExponent: Int
            try {
                firstExponent = getExp(firstRange)
            } catch (e: NotImplementedError) {
                //radians per day selected
                val map = Velocity.hoursMap(secondRange)
                val temp = map[topPosition, 200]
                val whichOne = if (temp == 200) map[bottomPosition] else temp
                ratio = bigDecimal()
                    .divide(BigDecimal(whichOne), mathContext)
                return basicFunction(-swapConversions())
            }
            ratio = bigDecimal()
                .multiply(
                    BigDecimal(60).pow(getExp(secondRange) - firstExponent, mathContext)
                )
            return basicFunction(-swapConversions())
        }
        intAssertOr(secondRange.last) {
            ratio =
                if (intAssertOr(firstRange.last))
                    bigDecimal()
                else {
                    val map = Velocity.hoursMap(firstRange)
                    val temp = map[topPosition, 200]
                    val whichOne = if (temp == 200) map[bottomPosition] else temp
                    bigDecimal().multiply(BigDecimal(whichOne))
                }
            return basicFunction(-swapConversions())
        }
        TODO() // should'nt reach here though
    }
}