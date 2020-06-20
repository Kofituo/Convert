package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import com.example.unitconverter.constants.Power
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Power(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = amongWatt() ?: wattConversions() ?: amongHorsepower() ?: ""

    private val minutesIndexes by lazy(LazyThreadSafetyMode.NONE) {
        intArrayOf(18, 24, 27, 30, 33)
    }

    private val hourIndexes by lazy(LazyThreadSafetyMode.NONE) {
        intArrayOf(19, 25, 28, 31, 34)
    }

    private fun amongWatt(): String? {
        rangeAssertAnd(0..19) {
            rangeAssertAnd(0..17) {
                return amongPrefixes(0..17, Power.amongWatt)
            }
            ratio = correctTime(ratio)
            Log.e("ratio", "$ratio")
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
    }

    private fun amongHorsepower(): String? {
        rangeAssertAnd(20..22) {
            ratio = if (rangeAssertAnd(21..22)) Power.impToElectricHp else correctHp { ratio }
            return result
        }
        return null
    }

    private fun amongCalories(): String? {
        rangeAssertAnd(23..28) {
            ratio = scaleByThousand { correctTime(ratio) }
            return result
        }
        return null
    }

    private fun scaleByThousand(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            when {
                rangeAssertOr(26..28) -> it.scaleByPowerOfTen(3)
                else -> it
            }
        }

    private fun wattConversions(): String? {
        rangeAssertOr(0..19) {
            when {
                rangeAssertOr(20..22) -> {
                    //to horsepower
                    ratio = correctHp { Power.wattToMetricHorsePower }
                    Log.e("hp", "$ratio")
                }
                rangeAssertOr(23..28) -> {
                    //to calorie per time
                    ratio = scaleByThousand { Power.wattToCalorie }
                    Log.e("cal", "$ratio")
                }
            }
            ratio = correctTime(ratio)
            Log.e("final", "$ratio")
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
    }

    private fun correctTime(bigDecimal: BigDecimal): BigDecimal {
        Log.e("start", "$bigDecimal")
        val topIsMinute = topPosition in minutesIndexes
        val bottomIsMinute = bottomPosition in minutesIndexes
        Log.e("1", "1")
        if (topIsMinute && bottomIsMinute) return bigDecimal
        val bottomIsHour = bottomPosition in hourIndexes
        val topIsHour = topPosition in hourIndexes
        Log.e("2", "2")
        if (bottomIsHour && topIsHour) return bigDecimal
        when {
            !topIsMinute && !bottomIsMinute && !topIsHour && !bottomIsHour -> {
                //it's none of them
                Log.e("3", "3")
                return bigDecimal
            }
            !topIsHour && !bottomIsHour -> {
                //the unit to minutes
                Log.e("4", "4")
                return bigDecimal.divide(60, mathContext)
            }
            !topIsMinute && !bottomIsMinute -> {
                //the unit to hours
                Log.e("5", "5")
                return bigDecimal.divide(3600, mathContext)
            }
        }
        /**
         * differentiate say cal per minute to joule per hour from
         * joule per minute to cal per hour
         * */
        //at this point its from min to hours
        val whichIsHour: Int
        val whichIsMinute =
            when {
                topIsMinute -> {
                    whichIsHour = bottomPosition
                    topPosition
                }
                bottomIsMinute -> {
                    whichIsHour = topPosition
                    bottomPosition
                }
                else -> TODO()
            }
        //hour to min  19  24
        //min to hour 18   25

        return when {
            whichIsHour > whichIsMinute -> {
                // minute to hour
                Log.e("6", "6  $ratio")
                bigDecimal.divide(60, mathContext)
            }
            else -> {
                //hour to minute
                Log.e("7", "7 $ratio")
                bigDecimal.multiply(60)
            }
        }
    }

    private inline fun correctHp(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            //from metric horsepower perspective
            when {
                intAssertOr(21) -> it.multiply(Power.metricToImpHp)
                intAssertOr(22) -> it.multiply(Power.metricToElectricHp)
                else -> it
            }
        }
}