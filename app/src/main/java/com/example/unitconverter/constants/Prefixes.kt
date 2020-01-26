package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.Utils.insertCommas
import java.math.BigDecimal
import java.math.MathContext

object Prefixes {
    // 1 to 24 for positive 1 to 24
    // 25 to 49 for negative 1 to 24
    var top = 0
    var bottom = 0
    fun buildPrefix(): SparseIntArray =
        SparseIntArray(31).apply {
            append(0, 24)//yotta
            append(1, 21)//zetta
            append(2, 18)//exa
            append(3, 15)//peta
            append(4, 12)//tera
            append(5, 9)//giga
            append(6, 6)//mego
            append(7, 3)//kilo
            append(8, 2)//hecto
            append(9, 1)//deca
            append(10, -1)//deci
            append(11, -2)//centi
            append(12, -3)//milli
            append(13, -6)//micro
            append(14, -9)//nano
            append(15, -12)//pico
            append(16, -15)//femto
            append(17, -18)//atto
            append(18, -21)
            append(19, -24)
        }

    fun prefix(top: Int, bottom: Int): Int = top - bottom

    fun internalPrefixMultiplication(x: String, pow: Int): String {
        if (x.isEmpty()) return ""
        val h = BigDecimal(x).times((BigDecimal.TEN).pow(pow, MathContext(30)))
        return h.stripTrailingZeros().insertCommas()
    }

    fun prefixMultiplication(x: String) =
        internalPrefixMultiplication(x, prefix(top, bottom))
}