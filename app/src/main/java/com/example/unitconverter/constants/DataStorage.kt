package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object DataStorage {

    inline val bitsToNibble get() = BigDecimal(4)

    inline val bytesToBit get() = BigDecimal("0.125")

    inline val nibbleToByte get() = BigDecimal(2)

    inline val biBitsToNibble get() = BigDecimal("0.25")

    inline val bitsToByte get() = BigDecimal(8)

    fun amongPrefixes(first: Int, start: Int = first + 1) = buildSparseIntArray(9) {
        append(first, 0)
        append(start, 24)//yotta
        append(start + 1, 21)//zetta
        append(start + 2, 18)//exa
        append(start + 3, 15)//peta
        append(start + 4, 12)//tera
        append(start + 5, 9)//giga
        append(start + 6, 6)//mego
        append(start + 7, 3)//kilo
    }

    fun biBits(start: Int) = buildSparseIntArray(8) {
        append(start, 80)
        append(start + 1, 70)
        append(start + 2, 60)
        append(start + 3, 50)
        append(start + 4, 40)
        append(start + 5, 30)
        append(start + 6, 20)
        append(start + 7, 10)
    }

    fun amongBi(start: Int) = buildSparseIntArray(9) {
        append(start, 24)//yotta
        append(start + 1, 21)//zetta
        append(start + 2, 18)//exa
        append(start + 3, 15)//peta
        append(start + 4, 12)//tera
        append(start + 5, 9)//giga
        append(start + 6, 6)//mego
        append(start + 7, 3)//kilo
    }
}