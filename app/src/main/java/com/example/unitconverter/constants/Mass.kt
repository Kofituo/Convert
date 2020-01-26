package com.example.unitconverter.constants

import android.util.SparseIntArray

import java.math.BigDecimal

object Mass : ConstantsInterface {

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
        get() = sixteen.multiply(2000)

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
        get() = BigDecimal.ONE.divide(7000, mathContext)

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
                .divide(200)

    val troyOunceToGramConstant: BigDecimal get() = BigDecimal("0.0311034768")

    val troyOunceToOunceConstant: BigDecimal
        get() = BigDecimal(192).divide(175, mathContext)

    val troyOunceToPoundConstant: BigDecimal
        get() = BigDecimal(175 * 16).divide(192, mathContext)

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

    val stoneToCaratConstant: BigDecimal get() = BigDecimal("31751.4659")

    val stoneToTroyPoundConstant: BigDecimal
        get() =
            BigDecimal(17) + BigDecimal("0.00518391280")
                .divide(gramToTroyPoundConstant, mathContext)

    val stoneToTroyOunceConstant: BigDecimal get() = stoneToTroyPoundConstant.multiply(12)

    val stoneToPennyWeightConstant: BigDecimal get() = stoneToTroyOunceConstant.multiply(20)

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
}