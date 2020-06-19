package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Power
import com.example.unitconverter.miscellaneous.isNotNull
import com.example.unitconverter.miscellaneous.isNull
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal
import kotlin.math.absoluteValue

class Power(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongWatt() ?: wattConversions() ?: amongCal() ?: amongHorsepower() ?: ""

    private val timeMap by lazy(LazyThreadSafetyMode.NONE) {
        mapOf(
            18 to 1,
            19 to 2,
            24 to 1,
            25 to 2,
            27 to 1,
            28 to 2,
            30 to 1,
            31 to 2,
            33 to 1,
            34 to 2
        )
    }

    //cal min to joule hour
    private inline fun finalTime(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            val topExponent = timeMap.getOrElse(topPosition) {
                0
            }
            val bottomExponent = timeMap.getOrElse(bottomPosition) {
                0
            }
            Log.e("tf", "$topExponent  $bottomExponent  $it")
            it.divide(BigDecimal(60).pow((topExponent - bottomExponent).absoluteValue), mathContext)
        }


    private fun amongWatt(): String? {
        rangeAssertAnd(0..19) {
            rangeAssertAnd(0..17) {
                return amongPrefixes(0..17, Power.amongWatt)
            }
            ratio = finalTime { ratio }
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
    }

    private fun wattConversions(): String? {
        rangeAssertOr(0..19) {
            ratio = when {
                rangeAssertOr(20..22) -> {
                    //to horsepower
                    correctHp { Power.wattToMetricHorsePower }
                }
                rangeAssertOr(23..28) -> {
                    ///to calorie
                    val f = finalTime { Power.wattToCalorie }
                    val p = scaleByThousand { f }
                    val wattRatio: BigDecimal?
                    Log.e("fin", "$p $f  $topPosition  $bottomPosition")
                    if (rangeAssertOr(18..19)) {
                        //joule to minute and hours conversion
                        wattRatio = if (intAssertOr(18)
                            && (intAssertOr(23) || intAssertOr(26))
                        ) {
                            Log.e("34", "45")
                            inverseOf(Power.wattToCalorie)
                        } else if (intAssertOr(19)
                            && (rangeAssertOr(26..27) || rangeAssertOr(23..24))
                        ) {
                            Log.e("90", "45")
                            inverseOf(Power.wattToCalorie)
                        } else null
                        ratio = scaleByThousand(wattRatio.isNotNull()) {
                            finalTime {
                                wattRatio ?: Power.wattToCalorie
                            }
                        }
                        Log.e("wa", "$wattRatio")
                        return basicFunction(if (wattRatio.isNull()) swapConversions() else -swapConversions())
                    }
                    p
                }
                else -> TODO()
            }
            //cal per second to joule per hour
            if (rangeAssertOr(18..19)) {
                //to minutes and to hours
                Log.e("int", "intr")
                ratio = finalTime { inverseOf(ratio) }
                return basicFunction(-swapConversions())
            }
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
    }

    private fun scaleByThousand(inverse: Boolean = false, bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            Log.e("sc", "$it")
            when {
                rangeAssertOr(26..28) -> it.scaleByPowerOfTen(if (inverse) -3 else 3)
                else -> it
            }
        }

    private fun correctKilCalVal(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            when {
                (topPosition - bottomPosition).absoluteValue <= 2 ->
                    BigDecimal(1000).divide(it, mathContext)
                else -> it.scaleByPowerOfTen(3)
            }
        }

    private fun amongCal(): String? {
        rangeAssertAnd(23..28) {
            //ratio =
            //finalTime { if (rangeAssertOr(23..25) && rangeAssertOr(26..28)) BigDecimal(1000) else ratio }
            finalTime { ratio }.apply {
                ratio =
                    if (rangeAssertOr(23..25) && rangeAssertOr(26..28))
                        correctKilCalVal { this }
                    else this
            }
            return result
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