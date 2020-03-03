package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.Time
import com.example.unitconverter.subclasses.Positions

class Time(override val positions: Positions) : ConstantsAbstractClass() {
    private inline val pow get() = swapConversions()

    override fun getText(): String =
        amongSeconds() ?: secondsConversions() ?: minuteConversions() ?: hourConversions()
        ?: daysConversions() ?: weekConversions() ?: ""

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
                    else -> TODO()
                }
                return basicFunction(pow)
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
                    else -> TODO()
                }
                return basicFunction(pow)
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
                    else -> TODO()
                }
                return basicFunction(pow)
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
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        return null
    }

    /*Helper functions */
    private inline fun time(block: Time.() -> Unit) = Time.apply(block)

    private inline fun prefix(block: (SparseIntArray) -> Unit) = Time.buildPrefix().also(block)
}