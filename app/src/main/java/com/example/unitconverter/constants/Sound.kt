package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object Sound {
    private val logEBase10 = BigDecimal("0.434294481903251827651128918916605")

    val neperToBel get() = BigDecimal(2).times(logEBase10)

    inline fun amongBel(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(3) {
            append(0, 0)
            append(1, -3)
            append(2, -1)
        }.also(block)
}