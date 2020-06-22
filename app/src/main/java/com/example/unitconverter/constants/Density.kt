package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray

object Density {

    val amongGramPerMetre
        get() = buildSparseIntArray(0) {
            append(0, 0)
            append(1, 3) // mm
            append(2, -3) // decimetre
            append(3, -9) //deca
            append(4, -15) //kilo
            append(5, 0) // gram per ml
            append(6, 0) // kg / litre
            append(7, -3) // kg / m
            append(8, 15) // kg / micro metre
            append(9, 6) // kg / mm
            append(10, 3) // kg / cm
            append(11, 0) // kg / dm
            append(12, -6) // kg /dam
            append(13, -12)
            append(14, 0) // ton / m
        }
}