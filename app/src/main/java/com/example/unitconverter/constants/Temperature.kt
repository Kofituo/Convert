package com.example.unitconverter.constants

import java.math.BigDecimal

object Temperature : ConstantsInterface {
    inline val ONE: BigDecimal get() = BigDecimal.ONE
    inline val ZERO: BigDecimal get() = BigDecimal.ZERO
    val celsiusToDelisleInverseRatio get() = BigDecimal(2).divide(3, mathContext)
    val celsiusToFahrenheitRatio get() = BigDecimal(9).divide(5)
    val celsiusToFahrenheitFixedValue get() = BigDecimal(32)
    val celsiusToKelvinFixedValue get() = BigDecimal("273.15")
    val celsiusToNewtonRatio: BigDecimal get() = BigDecimal(33).scaleByPowerOfTen(-2)
    val celsiusToDelisleRatio: BigDecimal get() = BigDecimal("1.5")
    val celsiusToRankineFixedValue: BigDecimal get() = BigDecimal("491.67")
    val celsiusToReaumur: BigDecimal get() = BigDecimal("0.8")
    val celsiusToRomerRatio: BigDecimal get() = BigDecimal(21).divide(40)
    val celsiusToRomerFixedValue: BigDecimal get() = BigDecimal(75).scaleByPowerOfTen(-1)
    val fahrenheitToKelvinFixedValue: BigDecimal get() = BigDecimal("-459.67")
    val fahrenheitToDelisleRatio: BigDecimal get() = BigDecimal(6).divide(5)
    val fahrenheitToKelvinRatio: BigDecimal get() = BigDecimal(5).divide(9, mathContext)
    val fahrenheitToNewtonRatio: BigDecimal get() = BigDecimal(60).divide(11, mathContext)
    val fahrenheitToRomerRatio: BigDecimal get() = BigDecimal(7).divide(24, mathContext)
    val fahrenheitToReaumurRatio: BigDecimal get() = BigDecimal(9).divide(4)
}