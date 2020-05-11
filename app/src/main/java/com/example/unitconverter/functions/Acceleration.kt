package com.example.unitconverter.functions

import com.example.unitconverter.constants.Acceleration
import com.example.unitconverter.constants.Angle
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Acceleration(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongRad() ?: radiansPerSecConversion() ?: amongDegrees() ?: degreesPerSecConversions()
        ?: amongGrad() ?: gradiansConversion() ?: amongRev()
        ?: TODO()

    private fun amongTimes(intRange: IntRange): String =
        // use time class to get result quickly
        intRange.iterator().run {
            val map = mapOf(
                nextInt() to 0,
                nextInt() to 1,
                nextInt() to 2
            )
            return Time(
                Positions(
                    map.getValue(bottomPosition),
                    map.getValue(topPosition),
                    inputString
                )
            ).getText()
        }

    private fun getExp(intRange: IntRange): Int {
        val map = Acceleration.amongSeconds(intRange)
        val first = map[topPosition, 200]
        if (first != 200) return first
        val second = map[bottomPosition, 200]
        if (second != 200) return second
        TODO()
    }

    private inline fun amongUnits(intRange: () -> IntRange) =
        if (rangeAssertAnd(intRange())) amongTimes(intRange()) else null

    private fun amongRad() = amongUnits { 0..2 }

    private fun amongDegrees() = amongUnits { 3..5 }

    private fun amongGrad() = amongUnits { 6..8 }

    private fun amongRev() = amongUnits { 9..11 }

    private fun radiansPerSecConversion(): String? {
        rangeAssertOr(0..2) {
            when {
                rangeAssertOr(3..5) -> {
                    //to degrees
                    return getConversion(0..2, 3..5) {
                        Angle.degreesToRadians
                    }
                }
                rangeAssertOr(6..8) -> {
                    //to gradians
                    return getConversion(0..2, 6..8) {
                        inverseOf(Angle.radiansToGradians)
                    }
                }
                rangeAssertOr(9..11) -> {
                    //to revolution
                    return getConversion(0..2, 9..11) {
                        inverseOf(Angle.radiansToRevolutions)
                    }
                }
                else -> TODO()
            }
        }
        return null
    }

    private fun degreesPerSecConversions(): String? {
        rangeAssertOr(3..5) {
            return when {
                rangeAssertOr(6..8) -> {
                    //to gradians
                    getConversion(3..5, 6..8) {
                        inverseOf(Angle.degreesToGradians)
                    }
                }
                rangeAssertOr(9..11) -> {
                    //to revolution
                    getConversion(3..5, 9..11) {
                        inverseOf(Angle.degreesToRevolution)
                    }
                }
                else -> TODO()
            }
        }
        return null
    }

    private fun gradiansConversion(): String? {
        rangeAssertOr(6..8) {
            rangeAssertOr(9..11) {
                //to revolution
                return getConversion(6..8, 9..11) {
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
                val map = Acceleration.hoursMap(secondRange)
                val temp = map[topPosition, 200]
                val whichOne = if (temp == 200) map[bottomPosition] else temp
                ratio = bigDecimal().divide(BigDecimal(whichOne), mathContext)
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
                    val map = Acceleration.hoursMap(firstRange)
                    val temp = map[topPosition, 200]
                    val whichOne = if (temp == 200) map[bottomPosition] else temp
                    bigDecimal().multiply(BigDecimal(whichOne))
                }
            return basicFunction(-swapConversions())
        }
        TODO() // should'nt reach here though
    }

    private class Time(override val positions: Positions) : ConstantsAbstractClass() {

        override fun getText(): String {
            when {
                intAssertOr(0) -> {
                    ratio = when {
                        intAssertOr(1) -> {
                            //to minutes
                            com.example.unitconverter.constants.Time.secondsToMinutes.pow(2)
                        }
                        intAssertOr(2) -> {
                            //to hours
                            com.example.unitconverter.constants.Time.secondsToHours.pow(2)
                        }
                        else -> TODO()
                    }
                }
                intAssertOr(1) -> {
                    intAssertOr(2) {
                        ratio = com.example.unitconverter.constants.Time.secondsToMinutes.pow(2)
                    }
                }
                else -> TODO()
            }
            return result
        }
    }
}