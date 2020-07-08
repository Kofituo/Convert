package com.otuolabs.unitconverter.functions

import android.util.SparseIntArray
import com.otuolabs.unitconverter.constants.Time
import com.otuolabs.unitconverter.subclasses.Positions

class Time(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongSeconds() ?: secondsConversions() ?: minuteConversions() ?: hourConversions()
        ?: daysConversions() ?: weekConversions() ?: fortnightConversions()
        ?: monthConversions() ?: calendarYearConversions() ?: yearExactConversions()
        ?: leapYearConversions() ?: decadeConversions() ?: centuryConversions()
        ?: millenniumConversions() ?: planckConversion()
        ?: TODO()

    private fun amongSeconds(): String? {
        // means its amongst the seconds family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            prefix {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun multipleSecondsPrefix(): Int {
        //to prevent double calling
        prefix {
            val temp = it[topPosition, -200]
            //which one is not seconds??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (topPosition > bottomPosition) 1 else -1
    }

    private fun secondsConversions(): String? {
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            time {
                ratio = when {
                    topPosition == 17 || bottomPosition == 17 -> {
                        //to minutes
                        secondsToMinutes
                    }
                    topPosition == 18 || bottomPosition == 18 -> {
                        //to hours
                        secondsToHours
                    }
                    topPosition == 19 || bottomPosition == 19 -> {
                        //to days
                        secondsToDay
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to week
                        weekToSeconds
                    }
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to fortnight
                        secondsToFortnight
                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        secondsToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        secondsToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        secondsToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        secondsToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        secondsToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToSeconds
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToSeconds
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToSeconds
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToSeconds
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(multipleSecondsPrefix())
            }
        }
        return null
    }

    private fun minuteConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17)
            time {
                ratio = when {
                    topPosition == 18 || bottomPosition == 18 -> {
                        // to hours
                        secondsToMinutes
                    }
                    topPosition == 19 || bottomPosition == 19 -> {
                        //to days
                        minutesToDay
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to weeks
                        weekToMinutes
                    }
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to fortnight
                        minutesToFortnight

                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        minutesToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        minutesToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        minutesToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        minutesToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        minutesToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToMinutes
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToMinutes
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToMinutes
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToMinutes
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun hourConversions(): String? {
        if (topPosition == 18 || bottomPosition == 18)
            time {
                ratio = when {
                    topPosition == 19 || bottomPosition == 19 -> {
                        //to days
                        hoursToDay
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to weeks
                        weekToHours
                    }
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to fortnight
                        hoursToFortnight
                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        hoursToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        hoursToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        hoursToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        hoursToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        hoursToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToHours
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToHours
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToHours
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToHours
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun daysConversions(): String? {
        if (topPosition == 19 || bottomPosition == 19)
            time {
                ratio = when {
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to weeks
                        weekToDays
                    }
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to fortnight
                        daysToFortnight
                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        daysToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        daysToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        daysToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        daysToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        daysToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToDays
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToDays
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToDays
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToDays
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun weekConversions(): String? {
        if (topPosition == 20 || bottomPosition == 20)
            time {
                ratio = when {
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to fortnight
                        weekToFortnight
                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        weeksToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        weekToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        weeksToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        weeksToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        weeksToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToWeeks
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToWeeks
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToWeek
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToWeek
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun fortnightConversions(): String? {
        if (topPosition == 21 || bottomPosition == 21)
            time {
                ratio = when {
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to months
                        fortnightToMonth
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        fortnightToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        fortnightToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        fortnightToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        fortnightToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToFortnight
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToFortnight
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToFortnight
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToFortnight
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun monthConversions(): String? {
        if (topPosition == 22 || bottomPosition == 22)
            time {
                ratio = when {
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to calendar year
                        monthToCalendarYear
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year exact
                        monthToYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        monthToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        monthsToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToMonth
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToMonth
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToMonth
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToMonth
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun calendarYearConversions(): String? {
        if (topPosition == 23 || bottomPosition == 23)
            time {
                ratio = when {
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to year
                        yearToCalendarYear
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        calendarYearToLeapYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        calendarYearToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToCalendarYear
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToCalendarYear
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToCalendarYear
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToCalendarYear
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun yearExactConversions(): String? {
        if (topPosition == 24 || bottomPosition == 24)
            time {
                ratio = when {
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to leap year
                        leapYearToYear
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        yearExactToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        yearExactToCentury
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        yearExactToMillennium
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToYearExact
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToYearExact
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun leapYearConversions(): String? {
        if (topPosition == 25 || bottomPosition == 25)
            time {
                ratio = when {
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to decade
                        leapYearToDecade
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        leapYearToCentury
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        leapYearToMillennium
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToLeapYear
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToLeapYear
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun decadeConversions(): String? {
        if (topPosition == 26 || bottomPosition == 26)
            time {
                ratio = when {
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to century
                        centuryToDecade
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToDecade
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToDecade
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToDecade
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun centuryConversions(): String? {
        if (topPosition == 27 || bottomPosition == 27)
            time {
                ratio = when {
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to millennium
                        millenniumToCentury
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToCentury
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToCentury
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun millenniumConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28)
            time {
                ratio = when {
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to planck time
                        planckTimeToMillennium
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to svedberg
                        svedbergToMillennium
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun planckConversion(): String? {
        if (topPosition == 29 || bottomPosition == 29)
            time {
                ratio = svedbergToPlanck
                return result
            }
        return null
    }

    /*Helper functions */
    private inline fun time(block: Time.() -> Unit) = Time.apply(block)
    private inline fun prefix(block: (SparseIntArray) -> Unit) = Time.buildPrefix().also(block)
}