package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.Length.footToMetre
import java.math.BigDecimal

object Speed : ConstantsInterface {

    val metrePerSecondToKmPerSecond: BigDecimal get() = inverseOf(BigDecimal("3.6"))

    val metrePerSecondToMiPerSecond get() = inverseOf(Length.metreToMile)

    val secondsToHours: BigDecimal get() = BigDecimal(3600)

    val secondsToMinutes: BigDecimal get() = BigDecimal(60)

    val kmpHToMiPerHour: BigDecimal
        get() = Length.metreToMile.scaleByPowerOfTen(-3).multiply(secondsToHours)

    val kmpHToMetrePerMinute get() = inverseOf(BigDecimal("0.06"))

    val metrePMinuteToMilePSecond: BigDecimal get() = Length.metreToMile.multiply(secondsToMinutes)

    val mpsToFootPerSecond get() = footToMetre

    val kmpHToFootPerSecond: BigDecimal get() = BigDecimal("1.09728")

    val milePerSecondToFootPerS: BigDecimal get() = BigDecimal(5280)

    val fpsToMetrePerMinute: BigDecimal get() = BigDecimal("18.2880")

    val kmPerHourToKnots get() = BigDecimal("1.852")

    val mpsToKnots
        get() = kmPerHourToKnots.multiply(1000).divide(3600, mathContext)

    private val metreToMile: BigDecimal get() = BigDecimal("1.609344")

    val milePerSecondToKnot: BigDecimal
        get() = metreToMile.multiply(3600).divide(kmPerHourToKnots, mathContext)

    val meterPerMinuteToKnot
        get() = kmPerHourToKnots.multiply(1000).divide(60, mathContext)

    val mpsToSpeedOfLight: BigDecimal get() = BigDecimal(299_792_458)

    val kmpHToSpeedOfLight: BigDecimal
        get() = BigDecimal(299_792_458).scaleByPowerOfTen(-3).multiply(secondsToHours)

    val miPerHourToC get() = Length.metreToMile.divide(299_792_458, mathContext)

    val metrePerMinuteToC get() = mpsToSpeedOfLight.multiply(60)

    val fpsToKnot: BigDecimal get() = kmPerHourToKnots.divide(kmpHToFootPerSecond, mathContext)

    val fpsToSpeedOfLight: BigDecimal get() = mpsToSpeedOfLight.divide(footToMetre, mathContext)

    val knotsToSpeedOfLight: BigDecimal get() = mpsToSpeedOfLight.divide(mpsToKnots, mathContext)

    fun buildPrefix() = buildSparseIntArray(17) {
        append(0, 0)
        append(1, 18)//exa
        append(2, 15)//peta
        append(3, 12)//tera
        append(4, 9)//giga
        append(5, 6)//mega
        append(6, 3)//kilo
        append(7, 2)//hecto
        append(8, 1)//deca
        append(9, -1)//deci
        append(10, -2)//centi
        append(11, -3)//milli
        append(12, -6)//micro
        append(13, -9)//nano
        append(14, -12)//pico
        append(15, -15)//femto
        append(16, -18)//atto
    }

    fun metreToMinute() = buildSparseIntArray(7) {
        append(21, 0)
        append(22, 3)//kilo
        append(23, 2)//hecto
        append(24, 1)//deca
        append(25, -1)//deci
        append(26, -2)//centi
        append(27, -3)//milli
    }
}