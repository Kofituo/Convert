package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.constants.Length.footToMetre
import com.example.unitconverter.constants.Length.inchToMetre
import com.example.unitconverter.constants.Length.metresToYard
import com.example.unitconverter.constants.Length.yardToFeet
import java.math.BigDecimal

object Volume : ConstantsInterface {
    val metreToInch get() = inchToMetre.cubed()

    val metreToFoot get() = footToMetre.cubed()

    val footToInch get() = Length.footToInch.cubed()

    val metreToYard get() = metresToYard.cubed()

    val inchToYard get() = Length.inchToYard.cubed()

    val feetToYard get() = yardToFeet.cubed()

    val metreToGallon: BigDecimal get() = BigDecimal("3.785411784").scaleByPowerOfTen(-3)

    val inchToGallon: BigDecimal get() = BigDecimal(231)

    val feetToGallon: BigDecimal get() = inchToGallon.divide(footToInch, mathContext)

    val yardToGallon: BigDecimal get() = inchToGallon.divide(inchToYard, mathContext)

    val metreToImpGallon: BigDecimal get() = BigDecimal("4.54609").scaleByPowerOfTen(-3)

    val inchToImpGallon: BigDecimal get() = metreToImpGallon.divide(metreToInch, mathContext)

    val feetToImpGallon: BigDecimal get() = metreToImpGallon.divide(metreToFoot, mathContext)

    val yardToImpGallon: BigDecimal get() = metreToImpGallon.divide(metreToYard, mathContext)

    val gallonToImpGallon: BigDecimal get() = metreToImpGallon.divide(metreToGallon, mathContext)

    val metreToPint: BigDecimal get() = BigDecimal("473.176473").scaleByPowerOfTen(-6)

    val inchToPint: BigDecimal get() = BigDecimal("28.875")

    val footToPint: BigDecimal get() = metreToPint.divide(metreToFoot, mathContext)

    val yardToPint: BigDecimal get() = metreToPint.divide(metreToYard, mathContext)

    val gallonToPint get() = inverseOf(BigDecimal(8))

    val impGallonToPint: BigDecimal get() = metreToPint.divide(metreToImpGallon, mathContext)

    val impGallonToImpPint get() = inverseOf(BigDecimal(8))

    val metreToImpPint: BigDecimal get() = BigDecimal("568.26125").scaleByPowerOfTen(-6)

    val inchToImpPint: BigDecimal get() = metreToImpPint.divide(metreToInch, mathContext)

    val feetToImpPint: BigDecimal get() = metreToImpPint.divide(metreToFoot, mathContext)

    val yardToImpPint: BigDecimal get() = metreToImpPint.divide(metreToYard, mathContext)

    val gallonToImpPint: BigDecimal get() = metreToImpPint.divide(metreToGallon, mathContext)

    val pintToImpPint: BigDecimal get() = metreToImpPint.divide(metreToPint, mathContext)

    val gallonToBarrel: BigDecimal get() = BigDecimal(42)

    val metreToBarrel: BigDecimal get() = gallonToBarrel.multiply(metreToGallon)

    val inchToBarrel: BigDecimal get() = BigDecimal(9702)

    val footToBarrel: BigDecimal get() = metreToBarrel.divide(metreToFoot, mathContext)

    val yardToBarrel: BigDecimal get() = metreToBarrel.divide(metreToYard, mathContext)

    val impGallonToBarrel: BigDecimal get() = metreToBarrel.divide(metreToImpGallon, mathContext)

    val usPintToBarrel: BigDecimal get() = BigDecimal(336)

    val impPintToBarrel: BigDecimal get() = metreToBarrel.divide(metreToImpPint, mathContext)

    val flOzToPint: BigDecimal get() = inverseOf(BigDecimal(16))

    val flOzToGallon: BigDecimal get() = inverseOf(BigDecimal(128))

    val metreToFlOz: BigDecimal get() = flOzToPint.multiply(metreToPint)

    val inchToFlOz: BigDecimal get() = metreToFlOz.divide(metreToInch, mathContext)

    val footToFlOz: BigDecimal get() = metreToFlOz.divide(metreToFoot, mathContext)

    val yardToFlOz: BigDecimal get() = metreToFlOz.divide(metreToYard, mathContext)

    val flOzToImpGallon: BigDecimal get() = metreToFlOz.divide(metreToImpGallon, mathContext)

    val flOzToImpPint: BigDecimal get() = metreToFlOz.divide(metreToImpPint, mathContext)

    val flOzToBarrel: BigDecimal get() = inverseOf(BigDecimal(5376))

    val impFluidOunceToImpPint: BigDecimal get() = inverseOf(BigDecimal(20))

    val impFluidOunceToImpGallon: BigDecimal get() = inverseOf(BigDecimal(160))

    val metreToImpFlOz: BigDecimal get() = impFluidOunceToImpPint.multiply(metreToImpPint)

    val inchToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToInch, mathContext)

    val footToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToFoot, mathContext)

    val yardToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToYard, mathContext)

    val gallonToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToGallon, mathContext)

    val pintToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToPint, mathContext)

    val barrelToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToBarrel, mathContext)

    val flOzToImpFlOz: BigDecimal get() = metreToImpFlOz.divide(metreToFlOz, mathContext)

    val quartToGallon: BigDecimal get() = BigDecimal("0.25")

    val metreToQuart: BigDecimal get() = quartToGallon.multiply(metreToGallon)

    val inchToQuart: BigDecimal get() = BigDecimal("57.75")

    val footToQuart: BigDecimal get() = metreToQuart.divide(metreToFoot, mathContext)

    val yardToQuart: BigDecimal get() = metreToQuart.divide(metreToYard, mathContext)

    val quartToImpGallon: BigDecimal get() = metreToQuart.divide(metreToImpGallon, mathContext)

    val quartToImpPint: BigDecimal get() = metreToQuart.divide(metreToImpPint, mathContext)

    val quartToImpFlOz: BigDecimal get() = metreToQuart.divide(metreToImpFlOz, mathContext)

    val quartToBarrel: BigDecimal get() = inverseOf(BigDecimal(168))

    val quartToPint: BigDecimal get() = BigDecimal(2)

    val quartToFlOz: BigDecimal get() = BigDecimal(32)

    val impQuartToImpGallon: BigDecimal get() = BigDecimal("0.25")

    val impQuartToImpPint: BigDecimal get() = BigDecimal(2)

    val impQuartToImpFlOz: BigDecimal get() = BigDecimal(40)

    val metreToImpQuart: BigDecimal get() = impQuartToImpPint.multiply(metreToImpPint)

    val inchToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToInch, mathContext)

    val footToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToFoot, mathContext)

    val yardToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToYard, mathContext)

    val gallonToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToGallon, mathContext)

    val pintToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToPint, mathContext)

    val barrelToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToBarrel, mathContext)

    val flOzToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToFlOz, mathContext)

    val quartToImpQuart: BigDecimal get() = metreToImpQuart.divide(metreToQuart, mathContext)

    fun metreToLitreMap() =
        SparseIntArray(8).apply {
            append(8, -3)
            append(9, 0)//kilo
            append(10, -1)//hecto
            append(11, -2)//deca
            append(12, -4)//deci
            append(13, -5)//centi
            append(14, -6)//milli
            append(15, -9)//micro
        }

    fun amongCubicMetreMap(): SparseIntArray =
        SparseIntArray(8).apply {
            //from cubic metre perspective
            append(0, 0)
            append(1, 3 * 3)//for kilo
            append(2, 2 * 3) //for hecto
            append(3, 1 * 3)//for deca
            append(4, -1 * 3)//for deci
            append(5, -2 * 3)//for centi
            append(6, -3 * 3)//for milli
            append(7, -6 * 3)//for micro
        }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun BigDecimal.cubed(): BigDecimal = pow(3)
}