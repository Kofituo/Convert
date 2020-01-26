package com.example.unitconverter.constants

import java.math.BigDecimal

object Temperature : ConstantsInterface {
    val celsiusToDelisleFixedValue get() = "100"
    val celsiusToDelisleConstant get() = BigDecimal(3).divide(2)
    val celsiusToFahrenheitConstant get() = BigDecimal(5).divide(9, mathContext)
    val celsiusToFahrenheitFixedValue get() = "32"
}