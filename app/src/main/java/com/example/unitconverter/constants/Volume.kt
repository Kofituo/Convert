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






    fun metreToLitreMap() =
        SparseIntArray(8).apply {
            append(8, -3)
            CharArray(9).size
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