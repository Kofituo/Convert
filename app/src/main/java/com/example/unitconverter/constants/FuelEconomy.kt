package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Length.footToInch
import com.example.unitconverter.constants.Length.footToMetre
import com.example.unitconverter.constants.Length.footToMile
import com.example.unitconverter.constants.Length.inchToMetre
import com.example.unitconverter.constants.Length.inchToMile
import com.example.unitconverter.constants.Length.metreToMile
import com.example.unitconverter.constants.Volume.gallonToImpGallon
import java.math.BigDecimal

object FuelEconomy {
    val metreToMileLitre get() = metreToMile

    val metreToFootLitre get() = footToMetre

    val metreToInchLitre get() = inchToMetre

    val mileToFootLitre get() = inverseOf(footToMile)

    val mileToInchLiter get() = inverseOf(inchToMile)

    val footToInchLitre get() = inverseOf(footToInch)

    private val litreToGallon get() = BigDecimal("3.785411784")

    val miPerUsGalToMPL: BigDecimal get() = metreToMile.divide(litreToGallon, mathContext)

    val litreToGallonMile get() = inverseOf(BigDecimal("3.785411784"))

    val miPerGalToFPL: BigDecimal get() = footToMile.divide(litreToGallon, mathContext)

    val miPerGalToIPL: BigDecimal get() = inchToMile.divide(litreToGallon, mathContext)

    private val litreToUkGal get() = BigDecimal("4.54609")

    val miPerUkGalToMPL: BigDecimal get() = metreToMile.divide(litreToUkGal, mathContext)

    val litreToUkGallonMile get() = inverseOf(BigDecimal("4.54609"))

    val miPerUkGalToFPL: BigDecimal get() = footToMile.divide(litreToUkGal, mathContext)

    val miPerUKGalToIPL: BigDecimal get() = inchToMile.divide(litreToUkGal, mathContext)

    val usGalToUkGalMile get() = inverseOf(gallonToImpGallon)

    fun sparseIntArray(start: Int) =
        buildSparseIntArray(5) {
            append(start, 0)
            append(start + 1, 3)
            append(start + 2, 2)
            append(start + 3, 1)
            append(start + 4, -1)
        }
}