package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import com.example.unitconverter.constants.Force
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Force(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..17) { Force.amongNewton } ?: newtonConversions()
        ?: amongPrefixes(18..19) { Force.gramToKg } ?: gramForceConversion() ?: amongPound()
        ?: poundalConversion() ?: TODO()

    private fun newtonConversions(): String? {
        rangeAssertOr(0..17) {
            ratio = when {
                rangeAssertOr(18..19) -> {
                    //to gram force
                    if (intAssertOr(18))
                        Force.newtonToKgForce.scaleByPowerOfTen(-3)
                    else Force.newtonToKgForce
                }
                intAssertOr(20) -> {
                    //to poundal
                    Force.poundalToNewton
                }
                rangeAssertOr(21..24) -> {
                    //to pound force and likes
                    correctPoundValue {
                        Force.newtonToPoundForce
                    }
                }
                else -> TODO()
            }
            return forMultiplePrefixes(
                swapConversions().also { innerMultiplePrefix(Force.amongNewton) }
            )
        }
        return null
    }

    private fun gramForceConversion(): String? {
        val isGramForce = intAssertOr(18)
        if (isGramForce || intAssertOr(19)) {
            ratio = when {
                intAssertOr(20) -> {
                    //to poundal
                    Force.kgForceToPoundal
                }
                rangeAssertOr(21..24) -> {
                    //to pound force and things
                    correctPoundValue {
                        Force.kgForceToPoundForce
                    }
                }
                else -> TODO()
            }
            ratio = correctKgForceVal { ratio }
            return result
        }
        return null
    }

    private fun poundalConversion(): String? {
        intAssertOr(20) {
            ratio = when {
                rangeAssertOr(21..24) -> {
                    //to pound force and things
                    correctPoundValue {
                        Force.poundalToPoundForce
                    }
                }
                else -> TODO()
            }
            return result
        }
        return null
    }

    private fun amongPound(): String? {
        rangeAssertAnd(21..24) {
            ratio = when {
                intAssertOr(21) -> {
                    //for pound force
                    val int = when {
                        intAssertOr(22) -> 1000
                        intAssertOr(23) -> 2000
                        else -> 2240
                    }
                    BigDecimal(int)
                }
                intAssertOr(22) -> {
                    //kip
                    if (intAssertOr(24))
                        BigDecimal("2.240")
                    else
                        BigDecimal(2)
                }
                intAssertOr(23) -> BigDecimal("1.12")

                else -> TODO()
            }
            return result
        }
        return null
    }

    //23 is for short ton
    //22 is kip
    private inline fun correctPoundValue(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            when {
                intAssertOr(23) -> it.multiply(2000)
                intAssertOr(22) -> it.multiply(1000)
                intAssertOr(24) -> it.multiply(2240)
                else -> it
            }
        }

    private inline fun correctKgForceVal(bigDecimal: () -> BigDecimal) =
        bigDecimal().let {
            when {
                intAssertOr(18) -> it.scaleByPowerOfTen(3)
                else -> it
            }
        }
}