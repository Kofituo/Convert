package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Mass.buildPrefixMass
import java.math.BigDecimal

object Energy {

    inline fun energy(block: Energy.() -> Unit) = apply(block)

    inline fun amongJoule(block: (SparseIntArray) -> Unit) =
        buildPrefixMass().apply { append(size(), -7) }.let(block)

    inline fun amongTNT(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(7) {
            append(18, -3)
            append(19, 0)
            append(20, 0)
            append(21, 3)//kilo
            append(22, 6)//mega //ton
            append(23, 9)//giga //kilo ton
            append(24, 12)//tera //mega ton
            append(25, 15) // giga ton
            append(26, 18)//tera ton
        }.also(block)

    inline fun amongWatt(block: (SparseIntArray) -> Unit) =
        buildSparseIntArray(4) {
            append(30, 0)
            append(31, -3)
            append(32, 3)
            append(33, 6)
        }.also(block)

    val jouleToCalorie get() = BigDecimal(4184)

    val jouleToFootPound get() = BigDecimal("1.3558179483314004")

    val footPoundToTnt: BigDecimal get() = jouleToFootPound.divide(jouleToCalorie, mathContext)

    val electronVoltToJoule: BigDecimal get() = BigDecimal(1602176634).scaleByPowerOfTen(-28)

    val eVToTNT get() = electronVoltToJoule.divide(4184, mathContext)

    val evToFootPound: BigDecimal
        get() = electronVoltToJoule.divide(jouleToFootPound, mathContext)

    val jouleToThermalUnit get() = BigDecimal("1055.06")

    val thermalUnitToTNT: BigDecimal get() = jouleToThermalUnit.divide(jouleToCalorie, mathContext)

    val thermalUnitToFootPound: BigDecimal
        get() = jouleToThermalUnit.divide(jouleToFootPound, mathContext)

    val thermalUnitToElectron: BigDecimal
        get() = jouleToThermalUnit.divide(electronVoltToJoule, mathContext)

    val jouleToWatt get() = BigDecimal(3600)

    val wattToTNT get() = jouleToWatt.divide(4184, mathContext)

    val wattToFootPound: BigDecimal get() = jouleToWatt.divide(jouleToFootPound, mathContext)

    val wattToeV: BigDecimal
        get() = jouleToWatt.divide(electronVoltToJoule, mathContext)

    val wattToThermalUnit: BigDecimal
        get() = jouleToWatt.divide(jouleToThermalUnit, mathContext)
}