package com.example.unitconverter.constants

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
    private val sixteen = BigDecimal(16)

    val gramToPoundConstant: BigDecimal get() = BigDecimal("0.45359237")

    val gramToOunceConstant: BigDecimal
        get() = gramToPoundConstant.divide(sixteen)

    val poundToOunceConstant: BigDecimal
        get() = BigDecimal.ONE.divide(sixteen)

    val shortTonToKgConstant get() = BigDecimal("907.18474")

    val shortTonToMetricTonConstant: BigDecimal
        get() = shortTonToKgConstant.scaleByPowerOfTen(-3)

    val shortTonToPoundConstant get() = BigDecimal(2000)

    val ounceToShortTonConstant: BigDecimal
        get() = sixteen.multiply(BigDecimal(2000))

    val gramToLonTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(gramToPoundConstant)

    val poundToLonTonConstant: BigDecimal get() = BigDecimal(2240)

    val shortTonToLongConstant: BigDecimal get() = BigDecimal("1.12")

    val metricTonToLonTonConstant: BigDecimal
        get() =
            poundToLonTonConstant.multiply(gramToPoundConstant.scaleByPowerOfTen(-3))

    val ounceToLongTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(sixteen)

    val gramToCaratConstant: BigDecimal
        get() = BigDecimal(200).scaleByPowerOfTen(-6)

    val poundToCaratConstant: BigDecimal
        get() =
            gramToCaratConstant.divide(gramToPoundConstant, mathContext)

    val ounceToCaratConstant: BigDecimal
        get() =
            gramToCaratConstant.divide(gramToPoundConstant, mathContext)
                .multiply(sixteen)

    val metricTonToCaratConstant: BigDecimal
        get() = gramToCaratConstant.scaleByPowerOfTen(-3)

    val shortTonToCaratConstant: BigDecimal get() = BigDecimal("4535923.7")

    val longTonToCaratConstant: BigDecimal get() = BigDecimal("5080234.544")

    val grainToGramConstant: BigDecimal get() = BigDecimal("64.79891").scaleByPowerOfTen(-6)

    val grainToPoundConstant: BigDecimal
        get() = BigDecimal.ONE.divide(BigDecimal(7000), mathContext)

    val grainToOunceConstant: BigDecimal get() = BigDecimal("437.5")

    val grainToMetricTonConstant: BigDecimal
        get() = grainToGramConstant.scaleByPowerOfTen(-3)

    val grainToShortTonConstant: BigDecimal
        get() = grainToGramConstant.divide(shortTonToKgConstant, mathContext)

    val grainToLongTonConstant: BigDecimal get() = BigDecimal(15_680_000)

    val grainToCaratConstant: BigDecimal
        get() = BigDecimal(200).divide(BigDecimal("64.79891"), mathContext)

    val grainToTroyPoundConstant: BigDecimal get() = BigDecimal(5760)

    val gramToTroyPoundConstant: BigDecimal
        get() = grainToGramConstant.multiply(grainToTroyPoundConstant)

    val troyPoundToPoundConstant: BigDecimal
        get() = grainToPoundConstant.multiply(grainToTroyPoundConstant)

    val troyPoundToOunceConstant: BigDecimal
        get() = troyPoundToPoundConstant.multiply(sixteen)

    val metricTonTroyPoundConstant: BigDecimal
        get() = gramToTroyPoundConstant.scaleByPowerOfTen(-3)

    val shortTonToTroyPound: BigDecimal
        get() =
            BigDecimal.valueOf(2000).divide(troyPoundToPoundConstant, mathContext)

    val longTonToTroyPoundConstant: BigDecimal
        get() =
            BigDecimal.valueOf(2240).divide(troyPoundToPoundConstant, mathContext)

    val caratToTroyPoundConstant: BigDecimal
        get() =
            BigDecimal("64.79891").multiply(grainToTroyPoundConstant)
                .divide(BigDecimal(200))

    val troyOunceToGramConstant: BigDecimal get() = BigDecimal("0.0311034768")

    val troyOunceToOunceConstant: BigDecimal
        get() = BigDecimal(192).divide(BigDecimal(175), mathContext)

    val troyOunceToPoundConstant: BigDecimal
        get() = BigDecimal(175 * 16).divide(BigDecimal(192), mathContext)

    val troyOunceToMetricTonConstant: BigDecimal
        get() = troyOunceToGramConstant.scaleByPowerOfTen(-3)

    val troyOunceToShortTonConstant: BigDecimal
        get() =
            BigDecimal.valueOf(2000).multiply(troyOunceToPoundConstant)

    val troyOunceToLongTonConstant: BigDecimal
        get() =
            BigDecimal.valueOf(2240).multiply(troyOunceToPoundConstant, mathContext)

    val caratToTroyOunceConstant: BigDecimal
        get() = BigDecimal("0.0311034768").divide(gramToCaratConstant)

    val troyOunceToGrainConstant: BigDecimal get() = BigDecimal(480)

    val troyOunceToTroyPoundConstant: BigDecimal get() = BigDecimal(12)

    val pennyWeightToGrainConstant: BigDecimal get() = BigDecimal(24)

    val pennyWeightToTroyOunceConstant: BigDecimal get() = BigDecimal(20)

    val pennyWeightToTroyPoundConstant: BigDecimal get() = BigDecimal(240)

    val pennyWeightToGramConstant: BigDecimal
        get() = BigDecimal("1.55517384").scaleByPowerOfTen(-3)

    val pennyWeightToMetricTonConstant: BigDecimal
        get() = BigDecimal("1.55517384").scaleByPowerOfTen(-6)

    val pennyWeightToShortTonConstant: BigDecimal
        get() =
            pennyWeightToGramConstant.divide(shortTonToKgConstant, mathContext)

    val pennyWeightToLongTonConstant: BigDecimal
        get() = pennyWeightToShortTonConstant.divide(shortTonToLongConstant, mathContext)

    val pennyWeightToCaratConstant: BigDecimal get() = BigDecimal("7.7758692")

    val stoneToPoundConstant: BigDecimal get() = BigDecimal(14)

    val pennyWeightToPoundConstant: BigDecimal
        get() =
            pennyWeightToGramConstant.divide(gramToPoundConstant, mathContext)

    val pennyWeightToOunceConstant: BigDecimal
        get() = pennyWeightToPoundConstant.multiply(sixteen)

    val stoneToOunceConstant: BigDecimal get() = stoneToPoundConstant.multiply(sixteen)

    val stoneToGramConstant: BigDecimal get() = BigDecimal("6.35029318")

    val stoneToMetricTonConstant: BigDecimal get() = stoneToGramConstant.scaleByPowerOfTen(-3)

    val stoneToShortTonConstant: BigDecimal get() = BigDecimal("0.007")

    val stoneToLonTonConstant: BigDecimal get() = BigDecimal("0.00625")

    val stoneToGrainConstant: BigDecimal get() = BigDecimal(98_000)

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
        BigDecimal(x).multiply((constant).pow(pow, mathContext))

    fun basicFunction(x: String, pow: Int): String? =
        basicConversionFunction(x, pow).stripTrailingZeros().insertCommas()

    //and vice versa
    fun somethingGramToPound(x: String, pow: Int): String? {
        val scale = prefix() * -pow
        return basicConversionFunction(x, pow)
            .scaleByPowerOfTen(scale).stripTrailingZeros().insertCommas()
    }

    fun gramToOunce(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun poundToOunce(x: String, pow: Int) =
        basicFunction(x, pow)

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
        basicFunction(x, -pow)

    fun grainToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun grainToShortTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun grainToLongTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun grainToCarat(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun troyPoundToGrain(x: String, pow: Int) =
        basicFunction(x, pow)

    fun gramToTroyPound(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun troyPoundToPound(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyPoundToOunce(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyPoundToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyPoundToShortTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun longTonToTroyPound(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun caratToTroyPound(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyOunceToGram(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun troyOunceToOunce(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyOunceToPound(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun troyOunceToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyOunceToShortTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun troyOunceToLongTon(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun troyOunceToCarat(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyOunceToGrain(x: String, pow: Int) =
        basicFunction(x, pow)

    fun troyOunceToTroyPound(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun pennyWeightToGram(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun pennyWeightToTroyOunce(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun pennyWeightToTroyPound(x: String, pow: Int) =
        basicFunction(x, -pow)

    fun pennyWeightToGrain(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToShortTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToLongTon(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToCarat(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToPound(x: String, pow: Int) =
        basicFunction(x, pow)

    fun pennyWeightToOunce(x: String, pow: Int) =
        basicFunction(x, pow)

    fun stoneToPound(x: String, pow: Int) =
        basicFunction(x, pow)

    fun stoneToOunce(x: String, pow: Int) =
        basicFunction(x, pow)

    fun stoneToGram(x: String, pow: Int) =
        somethingGramToPound(x, pow)

    fun stoneToMetricTon(x: String, pow: Int) =
        basicFunction(x, pow)


}