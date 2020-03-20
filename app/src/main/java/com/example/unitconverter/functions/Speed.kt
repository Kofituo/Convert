package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Speed
import com.example.unitconverter.constants.Time.secondsToHours
import com.example.unitconverter.constants.Time.secondsToMinutes
import com.example.unitconverter.subclasses.Positions

class Speed(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongMetrePerSecond() ?: amongMetrePerMinute() ?: metrePerSecondConversions()
        ?: kmpHConversions() ?: milePerSecondConversions() ?: metrePerMinuteConversions()
        ?: footPerSecondConversions() ?: knotConversion()
        ?: TODO()

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
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun setMileRatios(): Int {
        Speed.apply {
            if (topPosition == 18 || bottomPosition == 18)
                ratio = ratio.multiply(secondsToHours, mathContext)
            else if (topPosition == 19 || bottomPosition == 19)
                ratio = ratio.multiply(secondsToMinutes, mathContext)
            val pow = -simplifyMultiplePrefixMps()
            bottom = top
            top = 0
            return pow
        }
    }

    private fun setMileRatioInverse() {
        Speed.apply {
            if (topPosition == 18 || bottomPosition == 18) {
                ratio = ratio.divide(secondsToHours, mathContext)
            } else if (topPosition == 19 || bottomPosition == 19) {
                ratio = ratio.divide(secondsToMinutes, mathContext)
            }
        }
    }

    private fun multiplePrefixMetreToMinute(): Int {
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
                            multiplePrefixMetreToMinute() //get conversions among metre to minutes
                        top -= tempTop
                        bottom -= tempBottom
                        return forMultiplePrefixes(pow)
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to foot per second
                        ratio = mpsToFootPerSecond
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to knots
                        ratio = mpsToKnots
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to speed of light
                        ratio = mpsToSpeedOfLight
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
                        if (topPosition == 18 || bottomPosition == 18) {
                            ratio = ratio.divide(secondsToHours, mathContext)
                        } else if (topPosition == 19 || bottomPosition == 19) {
                            ratio = ratio.divide(secondsToMinutes, mathContext)
                        }
                        val pow = simplifyMultiplePrefixMps()
                        bottom = top
                        top = 0
                        return basicFunction(pow)
                    }
                    topPosition in 21..27 || bottomPosition in 21..27 -> {
                        //to metre per minute
                        ratio = kmpHToMetrePerMinute
                        return forMultiplePrefixes(multiplePrefixMetreToMinute())
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to foot per second
                        ratio = kmpHToFootPerSecond
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to knots
                        ratio = kmPerHourToKnots
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to speed of light
                        ratio = kmpHToSpeedOfLight
                    }
                    else -> TODO()
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
                        setMileRatios()
                        val tempTop = top
                        val tempBottom = bottom
                        multiplePrefixMetreToMinute()
                        ratio = metrePMinuteToMilePSecond
                        setMileRatioInverse()
                        top -= tempTop
                        bottom -= tempBottom
                        return forMultiplePrefixes(-pow)
                    }
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to feet per second
                        ratio = milePerSecondToFootPerS
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to knots
                        ratio = milePerSecondToKnot
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to speed of light
                        ratio = miPerHourToC
                    }
                    else -> TODO()
                }
                setMileRatioInverse()
                return basicFunction(-pow)
            }
        }
        return null
    }

    private fun metrePerMinuteConversions(): String? {
        if (topPosition in 21..27 || bottomPosition in 21..27) {
            Speed.apply {
                ratio = when {
                    topPosition == 28 || bottomPosition == 28 -> {
                        //to foot per second
                        fpsToMetrePerMinute
                    }
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to knots
                        meterPerMinuteToKnot
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to speed of light
                        metrePerMinuteToC
                    }
                    else -> TODO()
                }
                val pow = -multiplePrefixMetreToMinute()
                return forMultiplePrefixes(pow)
            }
        }
        return null
    }

    private fun footPerSecondConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28) {
            Speed.apply {
                ratio = when {
                    topPosition == 29 || bottomPosition == 29 -> {
                        //to knots
                        fpsToKnot
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to speed of light
                        fpsToSpeedOfLight
                    }
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun knotConversion(): String? {
        if (topPosition == 29 || bottomPosition == 29)
            if (topPosition == 30 || bottomPosition == 30) {
                ratio = Speed.knotsToSpeedOfLight
                return basicFunction(pow)
            }
        return null
    }
}