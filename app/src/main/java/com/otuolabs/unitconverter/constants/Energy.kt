package com.otuolabs.unitconverter.constants

import com.otuolabs.unitconverter.builders.buildSparseIntArray
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.divide
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.otuolabs.unitconverter.constants.Mass.buildPrefixMass
import java.math.BigDecimal

object Energy {

    inline fun energy(block: Energy.() -> Unit) = apply(block)

    val amongJoule
        get() = buildPrefixMass().apply { append(size(), -7) }

    val amongTNT
        get() =
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
            }

    val amongWatt
        get() = buildSparseIntArray(4) {
            append(30, 0)
            append(31, -3)
            append(32, 3)
            append(33, 6)
        }

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