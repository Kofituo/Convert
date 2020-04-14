package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Length.footToMetre
import com.example.unitconverter.constants.Length.inchToMetre
import com.example.unitconverter.constants.Luminance.feetToInch
import java.math.BigDecimal

object Illuminance {

    inline fun illuminance(block: Illuminance.() -> Unit) = apply(block)

    inline fun amongLux(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(9) {
            append(0, 0)
            append(1, 3)
            append(2, -6)
            append(3, 4)
            append(4, -3)//milli //nox
            append(5, -2) //correct
            append(6, 2) //correct
            append(7, 4)
            append(8, 6)
        }.also(block)

    val luxToFootCandle: BigDecimal get() = footToMetre.pow(-2, mathContext)

    val luxToLumenPerSqInch: BigDecimal get() = inchToMetre.pow(-2, mathContext)

    val inchToFoot get() = feetToInch

    /**
     * 1 lumen per square cm == 10000 lux
     * 1_000_000 lumen per square dam == 10000 lux
     * 100 lumen per decimetre == 10000 lux
     * 0.01 mm == 10000
     * */
}