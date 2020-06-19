package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Power
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

    private inline fun finalTime(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            val topExponent = timeMap.getOrElse(topPosition) {
                0
            }
            val bottomExponent = timeMap.getOrElse(bottomPosition) {
                0
            }
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
                rangeAssertOr(23..25) -> {
                    ///to calorie
                    TODO()
                    //correctTime { Power.wattToCalorie }
                }
                else -> TODO()
            }
            if (rangeAssertOr(18..19)) {
                //to minutes and to hours
                ratio = finalTime { inverseOf(ratio) }
                return basicFunction(-swapConversions())
            }
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Power.amongWatt) }
            )
        }
        return null
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