package com.example.unitconverter.constants

import android.util.SparseIntArray
import java.math.BigDecimal

object Length : ConstantsInterface {

    val footToInch: BigDecimal get() = BigDecimal(12)

    val footToMetre: BigDecimal get() = BigDecimal(3048).scaleByPowerOfTen(-4)

    val inchToYard: BigDecimal get() = BigDecimal(36)

    val inchToMetre: BigDecimal get() = footToMetre.divide(footToInch)

    val metreToMile: BigDecimal get() = BigDecimal("1609.344")

    val footToMile: BigDecimal get() = BigDecimal(5280)

    val inchToMile: BigDecimal get() = BigDecimal(63360)

    val mileToYard: BigDecimal get() = BigDecimal(1760)

    val yardToFeet: BigDecimal get() = BigDecimal(3)

    val metresToYard: BigDecimal get() = BigDecimal("0.9144")

    val metreToNauticalMile: BigDecimal get() = BigDecimal(1852)

    val nauticalMileToFoot: BigDecimal get() = metreToNauticalMile.divide(footToMetre, mathContext)

    val nauticalMileToInch: BigDecimal get() = nauticalMileToFoot.multiply(footToInch)

    val nauticalMileToMile: BigDecimal get() = metreToNauticalMile.divide(metreToMile, mathContext)

    val nauticalMileToYard: BigDecimal get() = metreToNauticalMile.divide(metresToYard, mathContext)

    fun metreConversions(): SparseIntArray =
        Mass.buildPrefixMass()
}