package com.example.unitconverter.constants

import java.math.BigDecimal
import java.math.MathContext

object BigDecimalsAddOns {
    val mathContext get() = MathContext(30)

    fun BigDecimal.divide(int: Int): BigDecimal = this.divide(BigDecimal(int))

    fun BigDecimal.divide(int: Int, mathContext: MathContext): BigDecimal =
        this.divide(BigDecimal(int), mathContext)

    fun BigDecimal.multiply(int: Int): BigDecimal = this.multiply(BigDecimal(int))

    fun BigDecimal.plus(string: String): BigDecimal = this.add(BigDecimal(string))

    fun BigDecimal.minus(string: String): BigDecimal = this.subtract(BigDecimal(string))

    fun inverseOf(bigDecimal: BigDecimal): BigDecimal =
        BigDecimal.ONE.divide(bigDecimal, mathContext)
}