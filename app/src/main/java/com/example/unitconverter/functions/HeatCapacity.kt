package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import com.example.unitconverter.constants.Energy
import com.example.unitconverter.constants.HeatCapacity
import com.example.unitconverter.constants.Temperature
import com.example.unitconverter.subclasses.Positions

class HeatCapacity(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..3, HeatCapacity.amongJoule) ?: jouleConversions() ?: calorieConversions()
        ?: btuConversion()
        ?: TODO()

/*
    private fun amongJoule(): String? {
        rangeAssertAnd(0..3) {
            HeatCapacity.amongJoule {
                return innerAmongPrefix(it)
            }
        }
        return null
    }
*/

    private fun joulePrefixes(): Int {
        //HeatCapacity.amongJoule {
        innerMultiplePrefix(HeatCapacity.amongJoule)
        //}
        return swapConversions()
    }

    private fun jouleConversions(): String? {
        rangeAssertOr(0..3) {
            HeatCapacity.heatCapacity {
                ratio = when {
                    intAssertOr(4) -> {
                        //calorie per kelvin
                        jouleToCalorie.scaleByPowerOfTen(-3)
                    }
                    intAssertOr(5) -> {
                        //kilo calorie per kelvin
                        jouleToCalorie
                    }
                    intAssertOr(6) -> {
                        ///btu per fahrenheit
                        jouleToBtu.multiply(Temperature.celsiusToFahrenheitRatio)
                    }
                    intAssertOr(7) -> {
                        ///btu per kelvin
                        jouleToBtu
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(joulePrefixes())
            }
        }
        return null
    }

    private fun calorieConversions(): String? {
        rangeAssertOr(4..5) {
            ratio = when {
                intAssertOr(6) -> {
                    Energy.thermalUnitToTNT
                        .multiply(Temperature.celsiusToFahrenheitRatio)
                }
                intAssertOr(7) -> {
                    Energy.thermalUnitToTNT
                }
                else -> TODO()
            }
            intAssertOr(4) {
                ratio = ratio.multiply(1000)
            }
            return result
        }
        return null
    }

    private fun btuConversion(): String? {
        intAssertOr(6) {
            intAssertOr(7) {
                ratio = inverseOf(Temperature.celsiusToFahrenheitRatio)
                return result
            }
        }
        return null
    }
}