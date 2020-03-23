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

    private inline val litreToGallon get() = BigDecimal("3.785411784")

    val miPerUsGalToMPL: BigDecimal get() = metreToMile.divide(litreToGallon, mathContext)

    val litreToGallonMile get() = inverseOf(BigDecimal("3.785411784"))

    val miPerGalToFPL: BigDecimal get() = footToMile.divide(litreToGallon, mathContext)

    val miPerGalToIPL: BigDecimal get() = inchToMile.divide(litreToGallon, mathContext)

    private inline val litreToUkGal get() = BigDecimal("4.54609")

    val miPerUkGalToMPL: BigDecimal get() = metreToMile.divide(litreToUkGal, mathContext)

    val litreToUkGallonMile get() = inverseOf(BigDecimal("4.54609"))

    val miPerUkGalToFPL: BigDecimal get() = footToMile.divide(litreToUkGal, mathContext)

    val miPerUKGalToIPL: BigDecimal get() = inchToMile.divide(litreToUkGal, mathContext)

    val usGalToUkGalMile get() = inverseOf(gallonToImpGallon)

    val litreToUSGalMetre get() = litreToGallon

    val litreToUkGalMetre get() = litreToUkGal

    val mipLToMetrePerGallon: BigDecimal get() = litreToGallon.multiply(metreToMile)

    val mipLToMetrePerUkGallon: BigDecimal get() = litreToUkGal.multiply(metreToMile)

    val feetPerLitreToMPUkGal: BigDecimal get() = litreToUkGal.multiply(footToMetre)

    val feetPerLitreToMPGal: BigDecimal get() = litreToGallon.multiply(footToMetre)

    val inchPerLitreToMPUkGal: BigDecimal get() = litreToUkGal.multiply(inchToMetre)

    val inchPerLitreToMPGal: BigDecimal get() = litreToGallon.multiply(inchToMetre)

    val miPerGalToMePerUkGal: BigDecimal
        get() = metreToMile
            .multiply(BigDecimal("4.54609").divide(BigDecimal("3.785411784"), mathContext))

    val miPerGalToMePerUsGal: BigDecimal get() = metreToMile

    val miPerUkGalToMePerUkGal: BigDecimal get() = metreToMile

    val miPerUkGalToMePerUsGal: BigDecimal
        get() = metreToMile
            .multiply(BigDecimal("3.785411784").divide(BigDecimal("4.54609"), mathContext))

    val mePerLitToLiPer100km get() = BigDecimal(100_000)

    val mePerUSGalToMePerUkGallon get() = gallonToImpGallon

    val mePerGalToLitrePer100km get() = (3.785411784 * 100 * 1000).toBigDecimal()

    val mePerUkGalToLitrePer100km get() = (4.54609 * 100 * 1000).toBigDecimal()

    val milePerLitreToLitrePer100km: BigDecimal
        get() = BigDecimal(100).divide(BigDecimal("1.609344"), mathContext)

    val feetPerLitreToLitrePer100km: BigDecimal
        get() = BigDecimal(100 * 10000 * 1000).divide(BigDecimal(3048), mathContext)

    val inchPerLitreToLitrePer100km: BigDecimal
        get() = BigDecimal(100 * 1000).divide(BigDecimal(0.0254), mathContext)

    val mpgUsLitreToLitrePer100km: BigDecimal
        get() = (100 * 3.785411784)
            .toBigDecimal()
            .divide(BigDecimal("1.609344"), mathContext)

    val mpgUkLitreToLitrePer100km: BigDecimal
        get() = (100 * 4.54609)
            .toBigDecimal()
            .divide(BigDecimal("1.609344"), mathContext)


    fun sparseIntArray(start: Int) =
        buildSparseIntArray(5) {
            append(start, 0)
            append(start + 1, 3)
            append(start + 2, 2)
            append(start + 3, 1)
            append(start + 4, -1)
        }
}