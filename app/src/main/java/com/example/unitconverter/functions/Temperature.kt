package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Temperature
import java.math.BigDecimal

class Temperature(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText() =
        celsiusConversions() ?: fahrenheitConversions()
        ?: ""

    private fun celsiusConversions(): String? {
        if (topPosition == 0 || bottomPosition == 0) {
            Temperature.apply {
                val pow = swapConversions()
                if (topPosition == 1 || bottomPosition == 1) {
                    // to fahrenheit
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = celsiusToFahrenheitRatio
                    //return complexConversionFunction(inputString, -pow)
                }
                if (topPosition == 2 || bottomPosition == 2) {
                    // to kelvin
                    fixedValue = celsiusToKelvinFixedValue
                    //return complexConversionFunction(inputString, -pow)
                }
                if (topPosition == 3 || bottomPosition == 3) {
                    // to newton
                    ratio = celsiusToNewtonRatio
                    //return complexConversionFunction(inputString, -pow)
                }
                if (topPosition == 4 || bottomPosition == 4) {
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
                }
                if (topPosition == 5 || bottomPosition == 5) {
                    // to Rankine
                    //like newton
                    ratio = celsiusToFahrenheitRatio //has the same value
                    if (pow == -1)
                        return complexConversionFunction(
                            celsiusToKelvinFixedValue.plus(inputString),
                            -pow
                        )
                    else {
                        fixedValue = celsiusToRankineFixedValue
                        //complexConversionFunction(inputString, -pow)
                    }
                }
                if (topPosition == 6 || bottomPosition == 6) {
                    ratio = celsiusToRomerRatio
                    fixedValue = celsiusToRomerFixedValue

                }
                if (topPosition == 7 || bottomPosition == 7) {
                    // to Réaumur
                    ratio = celsiusToReaumur
                    //return complexConversionFunction(inputString, -pow)
                }
                return complexConversionFunction(inputString, -pow)
            }
        }
        return null
    }

    private fun fahrenheitConversions(): String? {
        if (topPosition == 1 || bottomPosition == 1) {
            /**
             * For use with units such as temperature
             * eg the following is from Delisle to and vice versa::Notice the brackets
             * Celsius	[°C] = 100 − [°De] × ​2 ⁄ 3 ;fixed value is 100 constant is 2/3
             * [°De] = (100 − [°C]) × ​3 ⁄ 2
             * Fahrenheit	[°F] = 212 − [°De] × ​6⁄5
             * [°De] = (212 − [°F]) × ​5⁄6
             * Kelvin    [ K] = 373.15 − [°De] × ​2⁄3
             * [°De] = (373.15 − [ K]) × ​3 ⁄ 2
             * Rankine	[°R] = 671.67 − [°De] × ​6 ⁄ 5
             * [°De] = (671.67 − [°R]) × ​5 ⁄ 6
             *
             * constant is 32 since it does'nt change
             * use 9 / 5 as ratio
             *  1 (0°C × 9/5) + 32 = F
             *
             *  2 (0°F − 32) × 5/9
             *
             */
            Temperature.apply {
                val pow = swapConversions()
                if (topPosition == 2 || bottomPosition == 2) {
                    // to kelvin
                    fixedValue = fahrenheitToKelvinFixedValue
                    ratio = celsiusToFahrenheitRatio
                }
                if (topPosition == 3 || bottomPosition == 3) {
                    //to newton
                    ratio = fahrenheitToNewtonRatio
                    fixedValue = celsiusToFahrenheitFixedValue
                }
                if (topPosition == 4 || bottomPosition == 4) {
                    // to delisle
                    ratio = fahrenheitToDelisleRatio
                    return someDelisleConversions(pow, BigDecimal(212))
                }
                if (topPosition == 5 || bottomPosition == 5) {
                    // to rankine
                    fixedValue = fahrenheitToKelvinFixedValue
                }
                if (topPosition == 6 || bottomPosition == 6) {
                    //to romer
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = fahrenheitToRomerRatio
                    return someRomerConversions(inputString, -pow)
                }
                if (topPosition == 7 || bottomPosition == 7) {
                    // to reaumur
                    fixedValue = celsiusToFahrenheitFixedValue
                    ratio = fahrenheitToReaumurRatio
                }
                return complexConversionFunction(inputString, pow)
            }
        }
        return null
    }

    private fun someDelisleConversions(pow: Int, fixedValue: BigDecimal) =
        if (pow == -1)
            complexConversionFunction(fixedValue.minus(inputString), pow)
        else fixedValue
            .minus(BigDecimal(inputString).multiply(ratio))
            .toStringWithCommas()

    private fun someRomerConversions(
        string: String,
        pow: Int
    ): String {
        /**
         * for some romer conversions
         * e.g
         * Rømer
         * [°Rø] = ([°F] − 32) × ​7⁄24 + 7.5
         * [°F] = ([°Rø] − 7.5) × ​24⁄7 + 32
         */
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