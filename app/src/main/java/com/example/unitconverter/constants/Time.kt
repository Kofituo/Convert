package com.example.unitconverter.constants

import java.math.BigDecimal

object Time : ConstantsInterface {
    val secondsToMinutes: BigDecimal get() = BigDecimal(60)

    val secondsToHours: BigDecimal get() = BigDecimal(3600)

    val hoursToDay: BigDecimal get() = BigDecimal(24)

    val minutesToDay: BigDecimal get() = BigDecimal(24 * 60)

    val secondsToDay: BigDecimal get() = BigDecimal(24 * 60 * 60)

    val weekToDays: BigDecimal get() = BigDecimal(7)

    val weekToHours: BigDecimal get() = BigDecimal(7.toHours)

    val weekToMinutes: BigDecimal get() = BigDecimal(7.toHours.toMinutes)

    val weekToSeconds: BigDecimal get() = BigDecimal(7.toHours.toSeconds)

    val weekToFortnight: BigDecimal get() = BigDecimal(2)

    val daysToFortnight: BigDecimal get() = BigDecimal(14)

    val hoursToFortnight: BigDecimal get() = BigDecimal(14.toHours)

    val minutesToFortnight: BigDecimal get() = BigDecimal(14.toHours.toMinutes)

    val secondsToFortnight: BigDecimal get() = BigDecimal(14.toHours.toSeconds)

    val daysToMonth get() = BigDecimal(365).divide(12, mathContext)

    /*Helper functions */
    private inline val Int.toHours get() = this * 24

    private inline val Int.toMinutes get() = this * 60

    private inline val Int.toSeconds get() = this * 3600

    fun buildPrefix() = Mass.buildPrefixMass()
}