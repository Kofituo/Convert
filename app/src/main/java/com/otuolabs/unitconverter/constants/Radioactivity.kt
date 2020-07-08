package com.otuolabs.unitconverter.constants

import android.util.SparseIntArray
import com.otuolabs.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object Radioactivity {

    inline fun amongBecquerel(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(13) {
            append(0, 0)
            append(1, 24)//yotta
            append(2, 21)//zetta
            append(3, 18)//exa
            append(4, 15)//peta
            append(5, 12)//tera
            append(6, 9)//giga
            append(7, 6)//mega
            append(8, 3)//kilo
            append(9, 2)//hecto
            append(10, 1)//deca
            append(11, -1)//deci
            append(12, -2)//centi
            ////rutherford //////
            append(20, 6)
            append(21, 9) // kilo rutherford
            append(22, 6 - 2) //centi
            append(23, 6 - 3) // milli
            append(24, 6 - 6) // micro
            append(25, 6 - 9) //nano
            append(26, 6 - 12) // pico
        }.also(block)

    inline fun amongCurie(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(7) {
            append(13, 0)
            append(14, 3)
            append(15, -2)//centi
            append(16, -3)//milli
            append(17, -6)//micro
            append(18, -9)//nano
            append(19, -12)//pico
        }.also(block)

    val curieToBec get() = BigDecimal(37_000_000_000)
}