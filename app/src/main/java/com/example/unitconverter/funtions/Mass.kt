package com.example.unitconverter.funtions

import android.util.SparseIntArray
import com.example.unitconverter.Utils.insertCommas
import java.math.BigDecimal
import java.math.MathContext

object Mass {
    private val mathContext: MathContext get() = MathContext(30)

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

    val shortTonToMetricTonConstant: BigDecimal
        get() = shortTonToKgConstant.scaleByPowerOfTen(-3)

    val shortTonToPoundConstant get() = BigDecimal("2000")

    val ounceToShortTonConstant: BigDecimal
        get() = BigDecimal(16).multiply(BigDecimal(2000))

    val gramToLonTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(gramToPoundConstant)

    val poundToLonTonConstant: BigDecimal get() = BigDecimal(2240)

    val shortTonToLongConstant: BigDecimal get() = BigDecimal("1.12")

    val metricTonToLonTonConstant: BigDecimal
        get() =
            poundToLonTonConstant.multiply(gramToPoundConstant.scaleByPowerOfTen(-3))

    val ounceToLongTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(BigDecimal(16))

    val gramToCaratConstant: BigDecimal
        get() = BigDecimal(200).scaleByPowerOfTen(-6)

    val poundToCaratConstant: BigDecimal
        get() =
            gramToCaratConstant.divide(gramToPoundConstant, mathContext)

    val ounceToCaratConstant: BigDecimal
        get() =
            gramToCaratConstant.divide(gramToPoundConstant, mathContext)
                .multiply(BigDecimal(16))

    val metricTonToCaratConstant: BigDecimal
        get() = gramToCaratConstant.scaleByPowerOfTen(-3)

    val shortTonToCaratConstant: BigDecimal get() = BigDecimal("4535923.7")

    val longTonToCaratConstant: BigDecimal get() = BigDecimal("5080234.544")

    val grainToGramConstant: BigDecimal get() = BigDecimal("64.79891").scaleByPowerOfTen(-6)

    val grainToPoundConstant: BigDecimal
        get() = BigDecimal.ONE.divide(BigDecimal(7000), mathContext)

    val grainToOunceConstant: BigDecimal get() = grainToPoundConstant.multiply(BigDecimal(16))

    val grainToMetricTonConstant: BigDecimal
        get() = grainToGramConstant.scaleByPowerOfTen(-3)

    val grainToShortTonConstant: BigDecimal
        get() = grainToGramConstant.divide(shortTonToKgConstant, mathContext)

    val grainToLongTonConstant: BigDecimal
        get() = BigDecimal(15_680_000)

    fun buildPrefixMass(): SparseIntArray =
        SparseIntArray(17).apply {
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

    private fun prefix(): Int = Prefixes.prefix(top, bottom)

    fun prefixMultiplication(x: String): String =
        //Log.e("pre","${prefix()}  $top  $bottom")
        Prefixes.internalPrefixMultiplication(x, prefix())

    //must convert to string first
    private fun basicConversionFunction(x: String, pow: Int): BigDecimal =
        BigDecimal(x).multiply((constant).pow(pow, MathContext(30)))

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

    fun poundToMetricTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun ounceToMetricTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun poundToShortTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun gramToShortTon(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun ounceToShortTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun shortTonToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun poundToLongTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun metricTonToLongTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun shortTonToLongTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun gramToLongTon(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun ounceToLongTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun gramToCarat(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun ounceToCarat(x: String, pow: Int) =
        basicFunction(x, pow)

    fun poundToCarat(x: String, pow: Int) =
        basicFunction(x, pow)

    fun metricTonToCarat(x: String, pow: Int) =
        basicFunction(x, pow)

    fun shortTonToCarat(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun longTonToCarat(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun grainToGram(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun poundToGrain(x: String, pow: Int) =
        basicFunction(x, pow)

    fun ounceToGrain(x: String, pow: Int) =
        basicFunction(x, pow)

    fun grainToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun grainToShortTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun grainToLongTon(x: String, pow: Int) =
        basicFunction(x, -pow)
}