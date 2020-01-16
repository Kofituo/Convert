package com.example.unitconverter.funtions

import android.util.SparseIntArray
import com.example.unitconverter.Utils.insertCommas
import java.math.BigDecimal
import java.math.MathContext

object Mass {

    var top = 0x000
    var bottom = 0x000
    fun buildPrefixMass(): SparseIntArray =
        SparseIntArray(31).apply {
            append(0, 0)
            append(1, 18)//exa
            append(2, 15)//peta
            append(3, 12)//tera
            append(4, 9)//giga
            append(5, 6)//mego
            append(6, 3)//kilo
            append(7, 2)//hecto
            append(8, 1)//deca
            append(9, -1)//deci
            append(10, -2)//centi
            append(11, -3)//milli
            append(12, -6)//micro
            append(13, -9)//nano
            append(14, -12)//pico
            append(15, -15)//femto
            append(16, -18)//atto
        }

    private fun prefix(): Int {
        return Prefixes.prefix(top, bottom)
    }

    fun prefixMultiplication(x: String): String {
        //Log.e("pre","${prefix()}  $top  $bottom")
        return Prefixes.internalPrefixMultiplication(x, prefix())
    }

    //must convert to string first
    private fun kiloToPound(x: String, pow: Int): BigDecimal {
        val constant = BigDecimal("0.45359237")

        return BigDecimal(x).times((constant).pow(pow, MathContext(30)))
    }

    fun somethingGramToPound(x: String, pow: Int): String? {
        val scale = prefix() * -pow
        return kiloToPound(x, pow).scaleByPowerOfTen(scale).stripTrailingZeros().insertCommas()
    }

}

