package com.example.unitconverter.funtions

import android.util.SparseIntArray
import com.example.unitconverter.R
import com.example.unitconverter.Utils.insertCommas
import com.example.unitconverter.subclasses.RecyclerDataClass
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

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

    val shortTonToMetricTonConstant: BigDecimal
        get() = shortTonToKgConstant.scaleByPowerOfTen(-3)

    val shortTonToPoundConstant get() = BigDecimal("2000")

    val ounceToShortTonConstant: BigDecimal
        get() = BigDecimal(16).multiply(BigDecimal(2000))

    val gramToLonTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(gramToPoundConstant)

    val poundToLonTonConstant: BigDecimal get() = BigDecimal(2240)

    val shortTonToLongConstant: BigDecimal get() = BigDecimal(1.12)

    val metricTonToLonTonConstant: BigDecimal
        get() =
            poundToLonTonConstant.multiply(gramToPoundConstant.scaleByPowerOfTen(-3))

    val ounceToLongTonConstant: BigDecimal
        get() = poundToLonTonConstant.multiply(BigDecimal(16))

    val gramToCaratConstant: BigDecimal
        get() = BigDecimal(200).scaleByPowerOfTen(-6)

    val poundToCaratConstant: BigDecimal
        get() =
            gramToCaratConstant.divide(gramToPoundConstant, MathContext(30))

    val ounceToCaratConstant: BigDecimal
        get() = BigDecimal.ONE.divide(gramToCaratConstant)

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

    fun poundToMetricTon(x: String, pow: Int): String? =
        basicFunction(x, -pow)

    fun ounceToMetricTon(x: String, pow: Int): String? =
        basicFunction(x, -pow)

    fun poundToShortTon(x: String, pow: Int): String? {
        return basicFunction(x, pow)
    }

    fun gramToShortTon(x: String, pow: Int): String? =
        somethingGramToPound(x, pow)

    fun ounceToShortTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun shortTonToMetricTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun poundToLongTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun metricTonToLongTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun shortTonToLongTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun gramToLongTon(x: String, pow: Int): String? =
        somethingGramToPound(x, pow)

    fun ounceToLongTon(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun gramToCarat(x: String, pow: Int): String? =
        somethingGramToPound(x, pow)

    fun ounceToCarat(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun poundToCarat(x: String, pow: Int): String? =
        basicFunction(x, pow)

    fun buildForMass(
        getString: (Int) -> String,
        buildPrefixes: (String, String) -> MutableList<RecyclerDataClass>
    ): MutableList<RecyclerDataClass> {
        val gram =
            RecyclerDataClass(getString(R.string.gram), getString(R.string.gram_unit))
        val pound =
            RecyclerDataClass(getString(R.string.pound), getString(R.string.pound_unit))
        val ounce =
            RecyclerDataClass(getString(R.string.ounce), getString(R.string.ounce_unit))
        val metricTon =
            RecyclerDataClass(getString(R.string.metric_ton), getString(R.string.metricTonUnit))
        val shortTon =
            RecyclerDataClass(getString(R.string.short_ton), getString(R.string.short_ton_unit))
        val longTon =
            RecyclerDataClass(getString(R.string.long_ton), getString(R.string.long_ton_unit))
        val carat =
            RecyclerDataClass(getString(R.string.carat), getString(R.string.carat_unit))
        val grain =
            RecyclerDataClass(getString(R.string.grain), getString(R.string.grain_unit))
        val troyPound =
            RecyclerDataClass(getString(R.string.troy_pound), getString(R.string.troy_poundUnit))
        val troyOunce =
            RecyclerDataClass(getString(R.string.troy_ounce), getString(R.string.troyOunceUnit))
        val pennyweight =
            RecyclerDataClass(getString(R.string.pennyweight), getString(R.string.pennyweightUnit))
        val stone =
            RecyclerDataClass(getString(R.string.stone), getString(R.string.stone_unit))
        val atomicMassUnit =
            RecyclerDataClass(
                getString(R.string.atomicMassUnit),
                getString(R.string.atomic_mass_unit_unit)
            )
        val slugMass =
            RecyclerDataClass(getString(R.string.slug_mass), getString(R.string.slug_unit))
        val planckMass =
            RecyclerDataClass(getString(R.string.planck_mass), getString(R.string.planck_mass_unit))
        val solarMass =
            RecyclerDataClass(getString(R.string.solar_mass), getString(R.string.solar_mass_unit))

        return mutableListOf<RecyclerDataClass>().apply {
            gram.apply {
                add(this)
                quantity.toLowerCase(Locale.getDefault()).also {
                    addAll(buildPrefixes(it, correspondingUnit))
                }
            }
            add(pound)
            add(ounce)
            add(metricTon)
            add(shortTon)
            add(longTon)
            add(carat)
            add(grain)
            add(troyPound)
            add(troyOunce)
            add(pennyweight)
            add(stone)
            add(slugMass)
            add(atomicMassUnit)
            add(planckMass)
            add(solarMass)
        }
    }

}