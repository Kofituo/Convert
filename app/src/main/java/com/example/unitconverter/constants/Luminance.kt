package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.Angle.PI
import com.example.unitconverter.constants.Area.feetToMetre
import com.example.unitconverter.constants.Area.metreToInch
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import com.example.unitconverter.constants.Length.footToMetre
import java.math.BigDecimal

object Luminance {

    inline val apoStilbToCandela get() = inverseOf(PI)

    inline val lambertToCandela: BigDecimal get() = BigDecimal(10000).divide(PI, mathContext)

    inline val lambertToApostilb get() = BigDecimal(10000)

    inline val footLambertToCandelaFoot get() = PI

    inline val candelaToFoot: BigDecimal get() = footToMetre.pow(-2, mathContext)

    inline val candelaToFootLambert: BigDecimal get() = candelaToFoot.divide(PI, mathContext)

    inline val apostilbToFootLambert get() = candelaToFoot

    val apostilbToCandelaPerFoot: BigDecimal get() = PI.divide(feetToMetre, mathContext)

    val footLambertToLambert: BigDecimal
        get() = BigDecimal(3048)
            .scaleByPowerOfTen(-2)
            .pow(2, mathContext)

    val lambertToCandelaPerFoot: BigDecimal get() = PI.divide(footLambertToLambert, mathContext)

    val candelaPerInchToCanPerMetre get() = inverseOf(metreToInch)

    val apostilbToCandelaPerInch: BigDecimal get() = PI.divide(metreToInch, mathContext)

    inline val footLambertToCandelaInch get() = PI.multiply(144)

    inline val feetToInch get() = BigDecimal(144)

    fun candelaPerSquareMetre() = buildSparseIntArray(15) {
        append(0, 0)
        append(1, -6)
        append(2, -3)
        append(3, 3)
        append(4, 6)
        append(5, 9)
        append(6, 0) // nits
        append(7, 4) //stilb
        append(13, 6 * 2)//micro
        append(14, 3 * 2)//milli
        append(15, 2 * 2)//centi
        append(16, 1 * 2)//deci
        append(17, -1 * 2)//deca
        append(18, -2 * 2)//hecto
        append(19, -2 * 3)//kilo
    }
}