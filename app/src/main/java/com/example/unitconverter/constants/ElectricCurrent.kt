package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object ElectricCurrent {

    fun amongAmp() = buildSparseIntArray(6) {
        append(0, 0)
        append(1, 3)
        append(2, -2)
        append(3, -3)
        append(4, -6)
        append(5, -9)
    }

    inline val ampToBiot get() = BigDecimal(10)
}