package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import com.example.unitconverter.constants.Power
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Power(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongWatt() ?: wattConversions() ?: amongHorsepower() ?: horsepowerConversions()
        ?: amongCalories() ?: ""

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
            Log.e("among", "$ratio  ${swapConversions()}")
            return result
        }
        return null
    }

    private inline fun scaleByThousand(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            Log.e("scale", "$it")
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

    private fun horsepowerConversions(): String? {
        rangeAssertOr(20..22) {
            when {
                rangeAssertOr(23..28) -> {
                    //to calorie per time
                    ratio = scaleByThousand { Power.horsepowerToCalorie }
                }
            }
            ratio = correctTime(correctHpInverse { ratio })
            return result
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

        return when {
            !topIsMinute && !bottomIsMinute && !topIsHour && !bottomIsHour -> {
                //it's none of them
                Log.e("3", "3")
                bigDecimal
            }
            /**
             * differentiate
             * calorie per minute to joule per second from
             * joule per minute to calorie per second
             * */
            !topIsHour && !bottomIsHour -> {
                //the unit to minutes
                Log.e("4", "4")
                when {
                    whichIsWhich(topIsMinute) -> {
                        Log.e("4", "4.1")
                        bigDecimal.multiply(60)
                    }
                    else -> {
                        Log.e("4", "4.2")
                        bigDecimal.divide(60, mathContext)
                    }
                }
            }
            !topIsMinute && !bottomIsMinute -> {
                //the unit to hours
                Log.e("5", "5")
                when {
                    whichIsWhich(topIsHour) -> {
                        Log.e("5", "5.1")
                        bigDecimal.multiply(3600)
                    }
                    else -> {
                        Log.e("5", "5.2")
                        bigDecimal.divide(3600, mathContext)
                    }
                }
            }
            else -> {
                /**
                 * differentiate say cal per minute to joule per hour from
                 * joule per minute to cal per hour
                 * */
                //at this point its from min to hours
                when {
                    whichIsWhich(topIsMinute) -> {
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
        }

        /*val whichIsHour: Int
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
                else -> TODO() //redundant todo
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
        }*/
    }

    private fun whichIsWhich(topIs: Boolean): Boolean {
        val whichIsOther: Int
        val whichIsThis =
            when {
                topIs -> {
                    whichIsOther = bottomPosition
                    topPosition
                }
                else -> {
                    whichIsOther = topPosition
                    bottomPosition
                }
            }
        return whichIsOther > whichIsThis
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

    private inline fun correctHpInverse(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            //from metric horsepower perspective
            when {
                intAssertOr(21) -> it.multiply(Power.metricToImpHpInv)
                intAssertOr(22) -> it.multiply(Power.metricToElectricHpInv)
                else -> it
            }
        }
}