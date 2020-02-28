package com.example.unitconverter.functions

import com.example.unitconverter.constants.Speed
import com.example.unitconverter.subclasses.Positions

class Speed(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongMetrePerSecond() ?: amongMetrePerMinute() ?: metrePerSecondConversions()
        ?: kmpHConversions()
        ?: ""

    private inline val pow get() = swapConversions()

    private fun amongMetrePerSecond(): String? {
        // means its amongst the metre per second family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Speed.buildPrefix().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun amongMetrePerMinute(): String? {
        // means its amongst the metre per minute family
        if (topPosition in 21..27 && bottomPosition in 21..27) {
            Speed.metreToMinute().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun simplifyMultiplePrefixMps(): Int {
        //to prevent double calling
        Speed.buildPrefix().also {
            val temp = it[topPosition, -200]
            //which one is not metre per second??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition < bottomPosition) 1 else -1
        }
    }

    private fun setMileRatios(): Int {
        Speed.apply {
            if (topPosition == 18 || bottomPosition == 18)
                ratio = ratio.multiply(secondsToHours, mathContext)
            else if (topPosition == 19 || bottomPosition == 19)
                ratio = ratio.multiply(secondsToMinutes, mathContext)
            val pow = simplifyMultiplePrefixMps()
            bottom = top
            top = 0
            return pow
        }
    }

    private fun simplifyMultiplePrefixMetreToMinute(): Int {
        Speed.metreToMinute().apply {
            val temp = get(topPosition, 10)
            //which one is metre to minute
            val whichOne =
                if (temp == 10) get(bottomPosition) else temp
            top = whichOne
            bottom = 0
            return if (topPosition < bottomPosition) 1 else -1
        }
    }

    private fun metrePerSecondConversions(): String? {
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Speed.apply {
                when {
                    topPosition == 17 || bottomPosition == 17 -> {
                        //to km per hour
                        ratio = metrePerSecondToKmPerSecond
                    }
                    topPosition in 18..20 || bottomPosition in 18..20 -> {
                        //to mile per second
                        ratio = metrePerSecondToMiPerSecond
                        return forMultiplePrefixes(setMileRatios())
                    }
                    topPosition in 21..27 || bottomPosition in 21..27 -> {
                        //to metre to minute
                        ratio = secondsToMinutes
                        simplifyMultiplePrefixMps()
                        val tempTop = top
                        val tempBottom = bottom
                        val pow =
                            simplifyMultiplePrefixMetreToMinute() //get conversions among metre to minutes
                        top -= tempTop
                        bottom -= tempBottom
                        return forMultiplePrefixes(pow)
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(simplifyMultiplePrefixMps())
            }
        }
        return null
    }

    private fun kmpHConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Speed.apply {
                when {
                    topPosition in 18..20 || bottomPosition in 18..20 -> {
                        //to mile per second
                        ratio = kmpHToMiPerHour
                        if (topPosition == 18 || bottomPosition == 18)
                            ratio = ratio.divide(secondsToHours, mathContext)
                        else if (topPosition == 19 || bottomPosition == 19)
                            ratio = ratio.divide(secondsToMinutes, mathContext)
                        val pow = -simplifyMultiplePrefixMps()
                        bottom = top
                        top = 0

                        return basicFunction(pow)
                    }
                    topPosition in 21..27 || bottomPosition in 21..27 -> {
                        //to metre per minute
                        ratio = kmpHToMetrePerMinute
                        return forMultiplePrefixes(simplifyMultiplePrefixMetreToMinute())
                    }
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun milePerSecondConversions(): String? {
        if (topPosition in 18..20 || bottomPosition in 18..20) {
            Speed.apply {
                when {
                    topPosition in 21..27 || bottomPosition in 21..27 -> {
                        //to metre per minute

                    }
                }
            }
        }
        return null
    }
}