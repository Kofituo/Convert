package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import java.math.BigDecimal

object Mass : ConstantsInterface {

    private fun BigDecimal.toMetricTon(): BigDecimal =
        this.scaleByPowerOfTen(-3)

    private val sixteen = BigDecimal(16)

    val gramToPoundConstant: BigDecimal get() = BigDecimal("0.45359237")

    val gramToOunceConstant: BigDecimal
        get() = gramToPoundConstant.divide(sixteen)

    val poundToOunceConstant: BigDecimal
        get() = BigDecimal.ONE.divide(sixteen)

    val shortTonToKgConstant get() = BigDecimal("907.18474")

    val shortTonToMetricTonConstant: BigDecimal
        get() = shortTonToKgConstant.toMetricTon()

    val shortTonToPoundConstant get() = BigDecimal(2000)

    val ounceToShortTonConstant: BigDecimal
        get() = sixteen.multiply(2000)

    val gramToLonTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(gramToPoundConstant)

    val poundToLonTonConstant: BigDecimal get() = BigDecimal(2240)

    val shortTonToLongTonConstant: BigDecimal get() = BigDecimal("1.12")

    val metricTonToLonTonConstant: BigDecimal
        get() =
            poundToLonTonConstant.multiply(gramToPoundConstant.toMetricTon())

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
        get() = gramToCaratConstant.toMetricTon()

    val shortTonToCaratConstant: BigDecimal get() = BigDecimal("4535923.7")

    val longTonToCaratConstant: BigDecimal get() = BigDecimal("5080234.544")

    val grainToGramConstant: BigDecimal get() = BigDecimal("64.79891").scaleByPowerOfTen(-6)

    val grainToPoundConstant: BigDecimal
        get() = BigDecimal.ONE.divide(7000, mathContext)

    val grainToOunceConstant: BigDecimal get() = BigDecimal("437.5")

    val grainToMetricTonConstant: BigDecimal
        get() = grainToGramConstant.toMetricTon()

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
        get() = gramToTroyPoundConstant.toMetricTon()

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
        get() = troyOunceToGramConstant.toMetricTon()

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
        get() = BigDecimal("1.55517384").toMetricTon() //does'nt really mean to metric ton

    val pennyWeightToMetricTonConstant: BigDecimal
        get() = BigDecimal("1.55517384").scaleByPowerOfTen(-6)

    val pennyWeightToShortTonConstant: BigDecimal
        get() =
            pennyWeightToGramConstant.divide(shortTonToKgConstant, mathContext)

    val pennyWeightToLongTonConstant: BigDecimal
        get() = pennyWeightToShortTonConstant.divide(shortTonToLongTonConstant, mathContext)

    val pennyWeightToCaratConstant: BigDecimal get() = BigDecimal("7.7758692")

    val stoneToPoundConstant: BigDecimal get() = BigDecimal(14)

    val pennyWeightToPoundConstant: BigDecimal
        get() =
            pennyWeightToGramConstant.divide(gramToPoundConstant, mathContext)

    val pennyWeightToOunceConstant: BigDecimal
        get() = pennyWeightToPoundConstant.multiply(sixteen)

    val stoneToOunceConstant: BigDecimal get() = stoneToPoundConstant.multiply(sixteen)

    val stoneToGramConstant: BigDecimal get() = BigDecimal("6.35029318")

    val stoneToMetricTonConstant: BigDecimal get() = stoneToGramConstant.toMetricTon()

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

    val slugToPoundConstant: BigDecimal get() = BigDecimal("32.1740486948667165")

    val gramToSlugConstant: BigDecimal
        get() = slugToPoundConstant.multiply(gramToPoundConstant)

    val metricTonToSlugConstant: BigDecimal get() = gramToSlugConstant.toMetricTon()

    val slugToOunceConstant: BigDecimal get() = slugToPoundConstant.multiply(16)

    val slugToShortTonConstant: BigDecimal
        get() = slugToPoundConstant.divide(shortTonToPoundConstant)

    //(a / b) /c
    val slugToLongTonConstant: BigDecimal
        get() = slugToPoundConstant.divide(
            shortTonToPoundConstant.multiply(shortTonToLongTonConstant),
            mathContext
        )

    val slugToCaratConstant: BigDecimal
        get() = slugToPoundConstant.divide(poundToCaratConstant, mathContext)

    val slugToGrainConstant: BigDecimal get() = slugToPoundConstant.multiply(7000)

    val slugToTroyPoundConstant: BigDecimal
        get() = slugToPoundConstant.divide(troyPoundToPoundConstant, mathContext)

    val slugToTroyOunceConstant: BigDecimal get() = slugToTroyPoundConstant.multiply(12)

    val slugToPennyWeight: BigDecimal
        get() = slugToPoundConstant.divide(pennyWeightToPoundConstant, mathContext)

    val slugToStoneConstant: BigDecimal
        get() = slugToPoundConstant.divide(stoneToPoundConstant, mathContext)

    val amuToKg: BigDecimal get() = BigDecimal(16605390666).scaleByPowerOfTen(-37)//boom

    val amuToPound: BigDecimal get() = amuToKg.divide(gramToPoundConstant, mathContext)

    val amuToOunce: BigDecimal get() = amuToPound.multiply(16)

    val amuToMetricTon: BigDecimal get() = amuToKg.toMetricTon()

    val amuToShotTon: BigDecimal get() = amuToPound.divide(shortTonToPoundConstant, mathContext)

    val amuToLongTon: BigDecimal get() = amuToPound.divide(poundToLonTonConstant, mathContext)

    val amuToCarat: BigDecimal get() = amuToKg.divide(gramToCaratConstant, mathContext)

    val amuToGrain: BigDecimal get() = amuToKg.divide(grainToGramConstant, mathContext)

    val amuToTroyPound: BigDecimal get() = amuToGrain.divide(grainToTroyPoundConstant, mathContext)

    val amuToTroyOunce: BigDecimal
        get() = amuToTroyPound.multiply(troyOunceToTroyPoundConstant)

    val amuToPennyWeight: BigDecimal
        get() = amuToGrain.divide(pennyWeightToGrainConstant, mathContext)

    val amuToStone: BigDecimal get() = amuToPound.divide(stoneToPoundConstant, mathContext)

    val amuToSlug: BigDecimal get() = amuToPound.divide(slugToPoundConstant, mathContext)

    val planckMassToKg: BigDecimal get() = BigDecimal("2.176435").scaleByPowerOfTen(-8)

    val planckMassToPound: BigDecimal
        get() = planckMassToKg.divide(gramToPoundConstant, mathContext)

    val planckMassToMetricTon: BigDecimal get() = planckMassToKg.toMetricTon()

    val planckMassToOunce: BigDecimal get() = planckMassToPound.multiply(16)

    val plankMassToShortTon: BigDecimal
        get() = planckMassToPound.divide(shortTonToPoundConstant, mathContext)

    val planckMassToLongTon: BigDecimal
        get() = planckMassToPound.divide(poundToLonTonConstant, mathContext)

    val planckMassToCarat: BigDecimal
        get() = planckMassToKg.divide(gramToCaratConstant, mathContext)

    val planckMassToGrain: BigDecimal
        get() = planckMassToKg.divide(grainToGramConstant, mathContext)

    val planckMassToTroyPound: BigDecimal
        get() = planckMassToGrain.divide(grainToTroyPoundConstant, mathContext)

    val planckMassToTroyOunce: BigDecimal
        get() = planckMassToTroyPound.multiply(troyOunceToTroyPoundConstant)

    val planckMassToPennyWeight: BigDecimal
        get() = planckMassToGrain.divide(pennyWeightToGrainConstant, mathContext)

    val planckMassToStone: BigDecimal
        get() = planckMassToPound.divide(stoneToPoundConstant, mathContext)

    val planckMassToSlug: BigDecimal
        get() = planckMassToPound.divide(slugToPoundConstant, mathContext)

    val planckMassToAmu: BigDecimal
        get() = BigDecimal("7.62957532219899374").scaleByPowerOfTen(-20)

    val solarMassToKg: BigDecimal get() = BigDecimal("1.98847").scaleByPowerOfTen(30)

    val solarMassToPound: BigDecimal get() = solarMassToKg.divide(gramToPoundConstant, mathContext)

    val solarMassToMetricTon: BigDecimal get() = solarMassToKg.toMetricTon()

    val solarMassToOunce: BigDecimal get() = solarMassToPound.multiply(16)

    val solarMassToShortTon: BigDecimal
        get() = solarMassToPound.divide(shortTonToPoundConstant, mathContext)

    val solarMassToLongTon: BigDecimal
        get() = solarMassToPound.divide(poundToLonTonConstant, mathContext)

    val solarMassToCarat: BigDecimal get() = solarMassToKg.divide(gramToCaratConstant, mathContext)

    val solarMassToGrain: BigDecimal
        get() = solarMassToKg.divide(grainToGramConstant, mathContext)

    val solarMassToTroyPound: BigDecimal
        get() = solarMassToGrain.divide(grainToTroyPoundConstant, mathContext)

    val solarMassToTroyOunce: BigDecimal
        get() = solarMassToTroyPound.multiply(troyOunceToTroyPoundConstant)

    val solarMassToPennyWeight: BigDecimal
        get() = solarMassToGrain.divide(pennyWeightToGrainConstant, mathContext)

    val solarMassToStone: BigDecimal
        get() = solarMassToPound.divide(stoneToPoundConstant, mathContext)

    val solarMassToSlug: BigDecimal
        get() = solarMassToPound.divide(slugToPoundConstant, mathContext)

    val solarMassToAmu: BigDecimal
        get() = solarMassToKg.divide(amuToKg, mathContext)

    val solarMassToPlanckMass: BigDecimal
        get() = solarMassToAmu.divide(planckMassToAmu.pow(-1, mathContext), mathContext)

    fun buildPrefixMass() = buildSparseIntArray(17) {
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