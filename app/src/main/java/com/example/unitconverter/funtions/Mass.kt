package com.example.unitconverter.funtions

import android.util.SparseIntArray
import com.example.unitconverter.Utils.insertCommas
import java.math.BigDecimal
import java.math.MathContext

object Mass {
    var top: Int = 0x00
        set(value) {
            if (field != value) field = value
        }
    var bottom = 0x000
        set(value) {
            if (field != value) field = value
        }
    var constant: BigDecimal = BigDecimal.ZERO
        set(value) {
            if (field != value) field = value
        }
    val gramToPoundConstant: BigDecimal get() = BigDecimal("0.45359237")

    val gramToOunceConstant: BigDecimal
        get() = BigDecimal.ONE.divide(BigDecimal(16))
            .multiply(gramToPoundConstant)

    val poundToOunceConstant: BigDecimal
        get() = BigDecimal.ONE.divide(BigDecimal(16))

    val shortTonToKgConstant get() = BigDecimal("907.18474")

    val shortTonToPoundConstant get() = BigDecimal("2000")

    val ounceToShortTonConstant: BigDecimal
        get() = BigDecimal(16).multiply(BigDecimal(2000))

    fun buildPrefixMass(): SparseIntArray =
        SparseIntArray(31).apply {
            append(0, 0)
            append(1, 18)//exa
            append(2, 15)//peta
            append(3, 12)//tera
            append(4, 9)//giga
            append(5, 6)//mega
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
    private fun basicConversionFunction(x: String, pow: Int): BigDecimal {
        return BigDecimal(x).times((constant).pow(pow, MathContext(30)))
    }

    private fun basicFunction(x: String, pow: Int): String? =
        basicConversionFunction(x, pow).stripTrailingZeros().insertCommas()

    //and vice versa
    fun somethingGramToPound(x: String, pow: Int): String? {
        val scale = prefix() * -pow

        return basicConversionFunction(x, pow)
            .scaleByPowerOfTen(scale).stripTrailingZeros().insertCommas()
    }

    fun somethingToOunce(x: String, pow: Int, isScaled: Boolean): String? {
        return if (isScaled) {
            //for g to oz conversions
            somethingGramToPound(x, pow)
        } else basicFunction(x, pow) //for other conversions
    }

    fun poundToMetricTon(x: String, pow: Int): String? =
        basicFunction(x, -pow)

    fun ounceToMetricTon(x: String, pow: Int): String? =
        basicFunction(x, -pow)

    fun poundToShortTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun gramToShortTon(x: String, pow: Int): String? =
        somethingGramToPound(x, pow)

    fun ounceToShortTon(x: String, pow: Int): String? =
        basicFunction(x, pow)
}