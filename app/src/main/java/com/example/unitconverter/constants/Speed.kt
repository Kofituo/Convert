package com.example.unitconverter.constants

import android.util.SparseIntArray
import java.math.BigDecimal

object Speed : ConstantsInterface {

    val metrePerSecondToKmPerSecond: BigDecimal get() = BigDecimal("3.6")

    val metrePerSecondToMiPerSecond get() = inverseOf(metreToMile)

    val secondsToHours: BigDecimal get() = BigDecimal(3600)

    val secondsToMinutes: BigDecimal get() = BigDecimal(60)

    val kmpHToMiPerHour: BigDecimal
        get() = metreToMile.scaleByPowerOfTen(-3).multiply(secondsToHours)

    val kmpHToMetrePerMinute get() = inverseOf(BigDecimal("0.06"))

    val metreToMile: BigDecimal get() = metreToMile.multiply(secondsToMinutes)


    fun buildPrefix(): SparseIntArray =
        SparseIntArray(17).apply {
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

    fun metreToMinute(): SparseIntArray =
        SparseIntArray().apply {
            append(21, 0)
            append(22, 3)//kilo
            append(23, 2)//hecto
            append(24, 1)//deca
            append(25, -1)//deci
            append(26, -2)//centi
            append(27, -3)//milli
        }
}