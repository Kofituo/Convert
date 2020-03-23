package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object DataStorage {

    val bitsToNibble get() = BigDecimal(4)

    fun amongBits() = buildSparseIntArray(9) {
        append(0, 0)
        append(27, 24)//yotta
        append(28, 21)//zetta
        append(29, 18)//exa
        append(30, 15)//peta
        append(31, 12)//tera
        append(32, 9)//giga
        append(33, 6)//mego
        append(34, 3)//kilo
    }

    fun amongBytes() = buildSparseIntArray(9) {
        append(2, 0)
        append(19, 24)//yotta
        append(20, 21)//zetta
        append(21, 18)//exa
        append(22, 15)//peta
        append(23, 12)//tera
        append(24, 9)//giga
        append(25, 6)//mego
        append(26, 3)//kilo
    }
}