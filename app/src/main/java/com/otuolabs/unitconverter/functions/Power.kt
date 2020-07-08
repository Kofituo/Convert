package com.otuolabs.unitconverter.functions


import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.divide
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.multiply
import com.otuolabs.unitconverter.constants.Power
import com.otuolabs.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Power(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongWatt() ?: wattConversions() ?: amongHorsepower() ?: horsepowerConversions()
        ?: amongCalories() ?: calorieConversions() ?: amongTime(29..31) // foot pound
        ?: footPoundConversion() ?: amongTime(32..34) ?: TODO()

    private val minutesIndexes by lazy(LazyThreadSafetyMode.NONE) {
        listOf(18, 24, 27, 30, 33)
    }

    private val hourIndexes by lazy(LazyThreadSafetyMode.NONE) {
        listOf(19, 25, 28, 31, 34)
    }

    private fun amongWatt(): String? {
        rangeAssertAnd(0..19) {
            rangeAssertAnd(0..17) {
                return amongPrefixes(0..17) { Power.amongWatt }
            }
            ratio = correctTime(ratio)
            //Log.e("ratio", "$ratio")
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
            //Log.e("among", "$ratio  ${swapConversions()}")
            return result
        }
        return null
    }

    private fun amongTime(intRange: IntRange): String? {
        rangeAssertAnd(intRange) {
            ratio = correctTime(ratio)
            return result
        }
        return null
    }

    private inline fun scaleByThousand(inverse: Boolean = false, bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            ///Log.e("scale", "$it")
            when {
                rangeAssertOr(26..28) -> it.scaleByPowerOfTen(if (inverse) -3 else 3)
                else -> it
            }
        }

    private fun wattConversions(): String? {
        rangeAssertOr(0..19) {
            ratio = when {
                rangeAssertOr(20..22) -> {
                    //to horsepower
                    correctHp { Power.wattToMetricHorsePower }
                }
                rangeAssertOr(23..28) -> {
                    //to calorie per time
                    scaleByThousand { Power.wattToCalorie }
                }
                rangeAssertOr(29..31) -> {
                    //to foot pound
                    Power.footPoundToWatt
                }
                rangeAssertOr(32..34) -> {
                    //to btu
                    Power.wattToBTU
                }
                else -> TODO()
            }
            ratio = correctTime(ratio)
            //Log.e("final", "$ratio")
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
    }

    private fun horsepowerConversions(): String? {
        rangeAssertOr(20..22) {
            ratio = when {
                rangeAssertOr(23..28) -> {
                    //to calorie per time
                    scaleByThousand { Power.horsepowerToCalorie }
                }
                rangeAssertOr(29..31) -> {
                    //to foot-pound
                    Power.footPoundToHorsepower
                }
                rangeAssertOr(32..34) -> {
                    //to btu
                    Power.horsepowerToBTU
                }
                else -> TODO()
            }
            //Log.e("hp", "$ratio")
            ratio = correctTime(correctHpInverse { ratio })
            return result
        }
        return null
    }

    private fun calorieConversions(): String? {
        rangeAssertOr(23..28) {
            ratio = when {
                rangeAssertOr(29..31) -> {
                    //to foot pound
                    Power.footPoundToCalorie
                }
                rangeAssertOr(32..34) -> {
                    //to btu
                    Power.calorieToBTU
                }
                else -> TODO()
            }
            ratio = scaleByThousand(true) { correctTime(ratio) }
            return result
        }
        return null
    }

    private fun footPoundConversion(): String? {
        rangeAssertOr(29..31) {
            rangeAssertOr(32..34) {
                ratio = correctTime(Power.footPoundToBTU)
                return result
            }
        }
        return null
    }

    private fun correctTime(bigDecimal: BigDecimal): BigDecimal {
        //Log.e("start", "$bigDecimal")
        val topIsMinute = topPosition in minutesIndexes
        val bottomIsMinute = bottomPosition in minutesIndexes
        //Log.e("1", "1")
        if (topIsMinute && bottomIsMinute) return bigDecimal
        val bottomIsHour = bottomPosition in hourIndexes
        val topIsHour = topPosition in hourIndexes
        //Log.e("2", "2")
        if (bottomIsHour && topIsHour) return bigDecimal

        return when {
            !topIsMinute && !bottomIsMinute && !topIsHour && !bottomIsHour -> {
                //it's none of them
                //Log.e("3", "3")
                bigDecimal
            }
            /**
             * differentiate
             * calorie per minute to joule per second from
             * joule per minute to calorie per second
             * */
            !topIsHour && !bottomIsHour -> {
                //the unit to minutes
                //Log.e("4", "4")
                when {
                    whichIsWhich(topIsMinute) -> {
                        //Log.e("4", "4.1")
                        bigDecimal.multiply(60)
                    }
                    else -> {
                        //Log.e("4", "4.2")
                        bigDecimal.divide(60, mathContext)
                    }
                }
            }
            !topIsMinute && !bottomIsMinute -> {
                //the unit to hours
                //Log.e("5", "5")
                when {
                    whichIsWhich(topIsHour) -> {
                        //Log.e("5", "5.1")
                        bigDecimal.multiply(3600)
                    }
                    else -> {
                        //Log.e("5", "5.2")
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
                        //Log.e("6", "6  $ratio")
                        bigDecimal.divide(60, mathContext)
                    }
                    else -> {
                        //hour to minute
                        //Log.e("7", "7 $ratio")
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