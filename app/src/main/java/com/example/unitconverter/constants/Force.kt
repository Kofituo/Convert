package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import java.math.BigDecimal

object Force {
    val amongNewton
        get() = Mass.buildPrefixMass().apply { append(size(), -5) }

    val gramToKg
        get() = buildSparseIntArray(2) {
            append(18, 0)
            append(19, 3)
        }

    val newtonToKgForce get() = BigDecimal("9.80665")

    val poundalToNewton get() = BigDecimal("0.138254954376")

    val kgForceToPoundal: BigDecimal
        get() = poundalToNewton.divide(newtonToKgForce, mathContext)

    val newtonToPoundForce get() = BigDecimal("4.4482216152605")

    val kgForceToPoundForce get() = Mass.gramToPoundConstant

    val poundalToPoundForce: BigDecimal
        get() = newtonToPoundForce.divide(poundalToNewton, mathContext)
}