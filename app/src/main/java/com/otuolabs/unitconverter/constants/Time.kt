package com.otuolabs.unitconverter.constants

import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.divide
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.multiply
import java.math.BigDecimal

object Time {
    val secondsToMinutes: BigDecimal get() = BigDecimal(60)

    val secondsToHours: BigDecimal get() = BigDecimal(3600)

    val hoursToDay: BigDecimal get() = BigDecimal(24)

    val minutesToDay: BigDecimal get() = BigDecimal(24 * 60)

    val secondsToDay: BigDecimal get() = BigDecimal(24 * 60 * 60)

    val weekToDays: BigDecimal get() = BigDecimal(7)

    val weekToHours: BigDecimal get() = BigDecimal(7 * 24)

    val weekToMinutes: BigDecimal get() = BigDecimal((7 * 24) * 60)

    val weekToSeconds: BigDecimal get() = BigDecimal((7 * 24) * 3600)

    val weekToFortnight: BigDecimal get() = BigDecimal(2)

    val daysToFortnight: BigDecimal get() = BigDecimal(14)

    val hoursToFortnight: BigDecimal get() = BigDecimal(14 * 24)

    val minutesToFortnight: BigDecimal get() = BigDecimal((14 * 24) * 60)

    val secondsToFortnight: BigDecimal get() = BigDecimal((14 * 24) * 3600)

    val daysToMonth get() = BigDecimal(365).divide(12, mathContext)

    val weeksToMonth get() = BigDecimal(365).divide(12 * 7, mathContext)

    val hoursToMonth get() = BigDecimal(365 * 2)

    val minutesToMonth
        get() = BigDecimal((365 * 24) * 60).divide(12, mathContext)

    val secondsToMonth
        get() = BigDecimal((365 * 24) * 3600).divide(12, mathContext)

    val fortnightToMonth get() = BigDecimal(365).divide(12 * 14, mathContext)

    val hoursToCalendarYear get() = BigDecimal(365 * 24)

    val minutesToCalendarYear get() = BigDecimal((365 * 24) * 60)

    val secondsToCalendarYear get() = BigDecimal((365 * 24) * 3600)

    val daysToCalendarYear: BigDecimal get() = BigDecimal(365)

    val monthToCalendarYear: BigDecimal get() = BigDecimal(12)

    val weekToCalendarYear: BigDecimal get() = BigDecimal(365).divide(7, mathContext)

    val fortnightToCalendarYear get() = BigDecimal(365).divide(14, mathContext)

    val daysToYear get() = BigDecimal("365.25")

    val secondsToYear get() = BigDecimal(31_557_600)

    val minutesToYear get() = BigDecimal(31_557_600 / 60)

    val hoursToYear get() = BigDecimal(31_557_600 / 3600)

    val weeksToYear: BigDecimal get() = daysToYear.divide(7, mathContext)

    val fortnightToYear get() = daysToYear.divide(14, mathContext)

    val yearToCalendarYear get() = daysToYear.divide(365, mathContext)

    val monthToYear get() = yearToCalendarYear.multiply(12)

    val daysToLeapYear get() = BigDecimal(366)

    val weeksToLeapYear get() = BigDecimal(366).divide(7, mathContext)

    val hoursToLeapYear get() = BigDecimal(366 * 24)

    val minutesToLeapYear get() = BigDecimal((366 * 24) * 60)

    val secondsToLeapYear get() = BigDecimal((366 * 24) * 3600)

    val fortnightToLeapYear get() = BigDecimal(366).divide(14, mathContext)

    val monthToLeapYear: BigDecimal = secondsToLeapYear.divide(secondsToMonth, mathContext)

    val calendarYearToLeapYear get() = monthToLeapYear.divide(12, mathContext)

    val leapYearToYear: BigDecimal
        get() = BigDecimal(366).divide(BigDecimal("365.25"), mathContext)

    val calendarYearToDecade: BigDecimal get() = BigDecimal.TEN

    val daysToDecade get() = BigDecimal(10 * 365)

    val weeksToDecade = daysToDecade.divide(7, mathContext)

    val fortnightToDecade get() = daysToDecade.divide(14, mathContext)

    val hoursToDecade get() = BigDecimal(10 * 365 * 24)

    val minutesToDecade get() = BigDecimal(10 * 365 * (24 * 60))

    val secondsToDecade get() = BigDecimal(10 * 365 * (24 * 3600))

    val monthsToDecade get() = BigDecimal(120)

    val leapYearToDecade get() = BigDecimal(3650).divide(366, mathContext)

    val yearExactToDecade: BigDecimal
        get() = BigDecimal(3650).divide(BigDecimal("365.25"), mathContext)

    val centuryToDecade get() = BigDecimal(10)

    val centuryToCalendarYear get() = BigDecimal(100)

    val centuryToMonth get() = BigDecimal(1200)

    val centuryToDays get() = BigDecimal(100 * 365)

    val centuryToWeeks get() = centuryToDays.divide(7, mathContext)

    val centuryToFortnight get() = centuryToDays.divide(14, mathContext)

    val centuryToHours get() = BigDecimal(100 * (365 * 24))

    val centuryToMinutes get() = BigDecimal(100 * ((365 * 24) * 60))

    val centuryToSeconds get() = BigDecimal(100L * ((365 * 24) * 3600))

    val leapYearToCentury get() = BigDecimal(36500).divide(366, mathContext)

    val yearExactToCentury: BigDecimal
        get() = BigDecimal(36500).divide(BigDecimal("365.25"), mathContext)

    val millenniumToCalendarYear get() = BigDecimal(1000)

    val millenniumToDecade get() = BigDecimal(100)

    val millenniumToCentury get() = BigDecimal(10)

    val millenniumToDays get() = BigDecimal(1000 * 365)

    val millenniumToHours get() = BigDecimal(1000 * (365 * 24))

    val millenniumToMinutes get() = BigDecimal(1000 * ((365 * 24) * 60))

    val millenniumToSeconds get() = BigDecimal(1000L * ((365 * 24) * 3600))

    val millenniumToWeeks get() = millenniumToDays.divide(7, mathContext)

    val millenniumToFortnight get() = millenniumToDays.divide(14, mathContext)

    val millenniumToMonth get() = BigDecimal(1000).divide(12, mathContext)

    val yearExactToMillennium: BigDecimal
        get() = BigDecimal(365000).divide(BigDecimal("365.25"), mathContext)

    val leapYearToMillennium get() = BigDecimal(365000).divide(366, mathContext)

    val planckTimeToSeconds get() = BigDecimal("5.391247e-44")

    val planckTimeToMinutes get() = planckTimeToSeconds.divide(60, mathContext)

    val planckTimeToHours get() = planckTimeToSeconds.divide(3600, mathContext)

    val planckTimeToDays get() = planckTimeToSeconds.divide(3600 * 24, mathContext)

    val planckTimeToWeek get() = planckTimeToSeconds.divide(3600 * 24 * 7, mathContext)

    val planckTimeToFortnight
        get() = planckTimeToSeconds.divide(3600 * 24 * 14, mathContext)

    val planckTimeToMonth: BigDecimal
        get() = planckTimeToSeconds.divide(secondsToMonth, mathContext)

    val planckTimeToCalendarYear
        get() = planckTimeToSeconds.divide((365 * 24) * 3600, mathContext)

    val planckTimeToYearExact get() = planckTimeToSeconds.divide(31_557_600, mathContext)

    val planckTimeToLeapYear get() = planckTimeToSeconds.divide((366 * 24) * 3600, mathContext)

    val planckTimeToDecade
        get() = planckTimeToSeconds.divide(10 * 365 * (24 * 3600), mathContext)

    val planckTimeToCentury: BigDecimal
        get() = planckTimeToSeconds.divide(BigDecimal(100L * 365 * 24 * 3600), mathContext)

    val planckTimeToMillennium: BigDecimal
        get() = planckTimeToSeconds.divide(BigDecimal(1000L * ((365 * 24) * 3600)), mathContext)

    val svedbergToSeconds: BigDecimal get() = BigDecimal.ONE.scaleByPowerOfTen(-13)

    val svedbergToMinutes get() = svedbergToSeconds.divide(60, mathContext)

    val svedbergToHours get() = svedbergToSeconds.divide(3600, mathContext)

    val svedbergToDays get() = svedbergToSeconds.divide(3600 * 24, mathContext)

    val svedbergToWeek get() = svedbergToSeconds.divide(3600 * 24 * 7, mathContext)

    val svedbergToFortnight
        get() = svedbergToSeconds.divide(3600 * 24 * 14, mathContext)

    val svedbergToMonth: BigDecimal
        get() = svedbergToSeconds.divide(secondsToMonth, mathContext)

    val svedbergToCalendarYear
        get() = svedbergToSeconds.divide(365 * 24 * 3600, mathContext)

    val svedbergToYearExact get() = svedbergToSeconds.divide(31_557_600, mathContext)

    val svedbergToLeapYear get() = svedbergToSeconds.divide(366 * 24 * 3600, mathContext)

    val svedbergToDecade
        get() = svedbergToSeconds.divide(10 * 365 * 24 * 3600, mathContext)

    val svedbergToCentury: BigDecimal
        get() = svedbergToSeconds.divide(BigDecimal(100L * 365 * 24 * 3600), mathContext)

    val svedbergToMillennium: BigDecimal
        get() = svedbergToSeconds.divide(BigDecimal(1000L * 365 * 24 * 3600), mathContext)

    val svedbergToPlanck: BigDecimal
        get() = svedbergToSeconds.divide(planckTimeToSeconds, mathContext)

    fun buildPrefix() = Mass.buildPrefixMass()
}