package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.constants.Length.inchToMetre
import java.math.BigDecimal

object Volume : ConstantsInterface {
    val metreToInch = inchToMetre.cube()

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
    private inline fun BigDecimal.cube(): BigDecimal = pow(3)
}