package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.BigDecimalsAddOns.minus
import com.example.unitconverter.constants.BigDecimalsAddOns.plus
import com.example.unitconverter.constants.Temperature
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Temperature(override val positions: Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText() =
        celsiusConversions() ?: fahrenheitConversions()
        ?: kelvinConversions() ?: newtonConversions()
        ?: delisleConversions() ?: rankineConversions()
        ?: romerConversions()
        ?: throw Exception("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

    private fun celsiusConversions(): String? {
        if (topPosition == 0 || bottomPosition == 0) {
            Temperature.apply {
                if (topPosition == 1 || bottomPosition == 1) {
                    // to fahrenheit
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = celsiusToFahrenheitRatio
                    //return complexConversionFunction(inputString, -pow)
                } else if (topPosition == 2 || bottomPosition == 2) {
                    // to kelvin
                    fixedValue = celsiusToKelvinFixedValue
                    //return complexConversionFunction(inputString, -pow)
                } else if (topPosition == 3 || bottomPosition == 3) {
                    // to newton
                    ratio = celsiusToNewtonRatio
                    //return complexConversionFunction(inputString, -pow)
                } else if (topPosition == 4 || bottomPosition == 4) {
                    // to delisle
                    /**
                     * Delisle
                     * [°De] = (100 − [°C]) × ​3⁄2	[°C] = 100 − [°De] × ​2⁄3
                     * due to complex nature the code has some tweaks
                     * it would be like newton conversion (no fixedValue)
                     * [°N] = [°C] × ​33⁄100
                     * [°C] = [°N] × ​100⁄33
                     * (pow == 1) for the normal conversion
                     */
                    ratio = celsiusToDelisleRatio
                    val hundred = BigDecimal(100)
                    return if (pow == -1)
                        complexConversionFunction(hundred.minus(inputString), -pow)
                    //- for the first case --1 gives 1
                    else hundred
                        .subtract(BigDecimal(inputString).multiply(celsiusToDelisleInverseRatio))
                        .toStringWithCommas()
                } else if (topPosition == 5 || bottomPosition == 5) {
                    // to Rankine
                    //like newton
                    ratio = celsiusToFahrenheitRatio //has the same value
                    //complexConversionFunction(inputString, -pow)
                    if (pow == -1)
                        return complexConversionFunction(
                            celsiusToKelvinFixedValue.plus(inputString),
                            -pow
                        )
                    else fixedValue = celsiusToRankineFixedValue
                } else if (topPosition == 6 || bottomPosition == 6) {
                    ratio = celsiusToRomerRatio
                    fixedValue = celsiusToRomerFixedValue

                } else if (topPosition == 7 || bottomPosition == 7) {
                    // to Reaumur
                    ratio = celsiusToReaumur
                    //return complexConversionFunction(inputString, -pow)
                } else TODO()

                return complexConversionFunction(inputString, -pow)
            }
        }
        return null
    }

    private fun fahrenheitConversions(): String? {
        if (topPosition == 1 || bottomPosition == 1) {
            Temperature.apply {
                if (topPosition == 2 || bottomPosition == 2) {
                    // to kelvin
                    fixedValue = fahrenheitToKelvinFixedValue
                    ratio = celsiusToFahrenheitRatio
                } else if (topPosition == 3 || bottomPosition == 3) {
                    //to newton
                    ratio = fahrenheitToNewtonRatio
                    fixedValue = celsiusToFahrenheitFixedValue
                } else if (topPosition == 4 || bottomPosition == 4) {
                    // to delisle
                    ratio = fahrenheitToDelisleRatio
                    return someDelisleConversions(pow, BigDecimal(212))
                } else if (topPosition == 5 || bottomPosition == 5) {
                    // to rankine
                    fixedValue = fahrenheitToKelvinFixedValue
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to romer
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = fahrenheitToRomerRatio
                    return someRomerConversions(inputString, -pow)
                } else if (topPosition == 7 || bottomPosition == 7) {
                    // to reaumur
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = fahrenheitToReaumurRatio
                } else TODO()

                return complexConversionFunction(inputString, pow)
            }
        }
        return null
    }

    private fun kelvinConversions(): String? {
        if (topPosition == 2 || bottomPosition == 2) {
            Temperature.apply {
                if (topPosition == 3 || bottomPosition == 3) {
                    //to newton
                    fixedValue = celsiusToKelvinFixedValue
                    ratio = celsiusToNewtonRatio.pow(-1, mathContext)
                } else if (topPosition == 4 || bottomPosition == 4) {
                    //to delisle
                    ratio = celsiusToDelisleInverseRatio
                    return someDelisleConversions(pow, kelvinToDelisleFixedValue)
                } else if (topPosition == 5 || bottomPosition == 5) {
                    //to rankine
                    ratio = celsiusToFahrenheitRatio.pow(-1, mathContext)
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to romer
                    ratio = celsiusToRomerRatio
                    fixedValue = celsiusToKelvinFixedValue
                    return someRomerConversions(inputString, -pow)
                } else if (topPosition == 7 || bottomPosition == 7) {
                    // to reaumur
                    fixedValue = celsiusToKelvinFixedValue
                    ratio = celsiusToReaumur.pow(-1, mathContext)
                } else TODO()

                return complexConversionFunction(inputString, pow)
            }
        }
        return null
    }

    private fun newtonConversions(): String? {
        if (topPosition == 3 || bottomPosition == 3) {
            Temperature.apply {
                if (topPosition == 4 || bottomPosition == 4) {
                    //to delisle
                    ratio = newtonToDelisleRatio
                    return someDelisleConversions(pow, BigDecimal(33))
                }
                if (topPosition == 5 || bottomPosition == 5) {
                    //to rankine
                    ratio = fahrenheitToNewtonRatio
                    fixedValue = celsiusToRankineFixedValue
                    return complexConversionFunction(inputString, -pow)
                }
                if (topPosition == 6 || bottomPosition == 6) {
                    //to romer
                    ratio = newtonToRomerRatio
                    return someRomerConversions(inputString, -pow)
                }
                if (topPosition == 7 || bottomPosition == 7) {
                    //to reaumur
                    ratio = newtonToReaumurRatio
                    return complexConversionFunction(inputString, pow)
                }
            }
        }
        return null
    }

    private fun delisleConversions(): String? {
        if (topPosition == 4 || bottomPosition == 4) {
            Temperature.apply {
                if (topPosition == 5 || bottomPosition == 5) {
                    //to rankine
                    ratio = fahrenheitToDelisleRatio
                    return someDelisleConversions(-pow, BigDecimal("671.67"))
                }
                if (topPosition == 6 || bottomPosition == 6) {
                    //to romer
                    ratio = delisleToRomerRatio
                    return someDelisleConversions(-pow, BigDecimal(60))
                }
                if (topPosition == 7 || bottomPosition == 7) {
                    ratio = delisleToReaumurRatio
                    return someDelisleConversions(-pow, BigDecimal.valueOf(80))
                }
            }
        }
        return null
    }

    private fun rankineConversions(): String? {
        if (topPosition == 5 || bottomPosition == 5) {
            Temperature.apply {
                if (topPosition == 6 || bottomPosition == 6) {
                    // to romer
                    ratio = fahrenheitToRomerRatio
                    fixedValue = celsiusToRankineFixedValue
                    return someRomerConversions(inputString, -pow)
                }
                if (topPosition == 7 || bottomPosition == 7) {
                    // to reaumur
                    fixedValue = celsiusToRankineFixedValue
                    ratio = fahrenheitToReaumurRatio
                    return complexConversionFunction(inputString, pow)
                }
            }
        }
        return null
    }

    private fun romerConversions(): String? {
        if (topPosition == 6 || bottomPosition == 6) {
            if (topPosition == 7 || bottomPosition == 7) {
                ratio = BigDecimal(21).divide(32, mathContext)
                return someRomerConversions(inputString, pow)
            }
        }
        return null
    }

    private fun someDelisleConversions(pow: Int, fixedValue: BigDecimal) =
        /**
         * for things like this
         *
         * [°De] = (373.15 − [ K]) × ​3⁄2 --- for pow == -1
         *
         * [ K] = 373.15 − [°De] × ​2⁄3 ---- 2
         * */
        if (pow == -1)
            complexConversionFunction(fixedValue.minus(inputString), pow)
        else fixedValue
            .minus(BigDecimal(inputString).multiply(ratio))
            .toStringWithCommas()

    /**
     * for some romer conversions
     * e.g
     *
     * [°Rø] = ([°F] − 32) × ​7⁄24 + 7.5  (pow == 1)
     *
     * [°F] = ([°Rø] − 7.5) × ​24⁄7 + 32
     */
    private fun someRomerConversions(
        string: String,
        pow: Int
    ): String {
        val romerConstant = BigDecimal("7.5")
        return if (pow == 1) {
            //first case ([°F] − 32) × ​7⁄24 + 7.5
            BigDecimal(string)
                .subtract(fixedValue)
                .multiply(ratio)
                .add(romerConstant)
                .toStringWithCommas()
        } else {
            //second case ([°Rø] − 7.5) × ​24⁄7 + 32
            BigDecimal(string)
                .subtract(romerConstant)
                .multiply(ratio.pow(-1, mathContext))
                .add(fixedValue)
                .toStringWithCommas()
        }
    }
}