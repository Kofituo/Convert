package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import java.math.BigDecimal

object Pressure {

    val mmOfHgToPascal get() = BigDecimal("133.322387415")

    val pascalToInchOfHg: BigDecimal get() = mmOfHgToPascal.multiply(BigDecimal("25.4"))

    val mmOfHgToInchOfHg get() = inverseOf(BigDecimal("25.4"))

    val kgForceToPascal: BigDecimal get() = BigDecimal("98066.5")

    val gramForceToPascal: BigDecimal get() = BigDecimal("98.0665")

    val pascalToPsi: BigDecimal by lazy {
        BigDecimal("4.4482216152605").divide(BigDecimal("0.00064516"), mathContext)
    }

    val inchOfHgToGramForce: BigDecimal
        get() = gramForceToPascal.divide(pascalToInchOfHg, mathContext)

    val inchOfHgToKgForce: BigDecimal get() = kgForceToPascal.divide(pascalToInchOfHg, mathContext)

    val inchOfHgToPsi: BigDecimal get() = pascalToPsi.divide(pascalToInchOfHg, mathContext)

    val mercuryToKgForce: BigDecimal get() = kgForceToPascal.divide(mmOfHgToPascal, mathContext)

    val mercuryToGramForce: BigDecimal
        get() = gramForceToPascal.divide(mmOfHgToPascal, mathContext)

    val mercuryToPsi: BigDecimal get() = pascalToPsi.divide(mmOfHgToPascal, mathContext)

    val pascalToAtm: BigDecimal get() = BigDecimal(101325)

    val inchOfHgToAtm: BigDecimal get() = pascalToAtm.divide(pascalToInchOfHg, mathContext)

    val mercuryToAtm: BigDecimal get() = pascalToAtm.divide(mmOfHgToPascal, mathContext)

    val gramForceToPsi: BigDecimal get() = pascalToPsi.divide(gramForceToPascal, mathContext)

    val gramForceToKgForce: BigDecimal get() = BigDecimal(1000)

    val kgForceToPsi: BigDecimal get() = pascalToPsi.divide(kgForceToPascal, mathContext)

    val gramForceToAtm: BigDecimal get() = pascalToAtm.divide(gramForceToPascal, mathContext)

    val kgForceToAtm: BigDecimal get() = pascalToAtm.divide(kgForceToPascal, mathContext)

    val psiToAtm: BigDecimal get() = pascalToAtm.divide(pascalToPsi, mathContext)

    val pascalToTorr get() = BigDecimal(101325).divide(760, mathContext)

    val torrToAtm get() = inverseOf(BigDecimal(760))

    val inchOfHgToTorr: BigDecimal get() = pascalToTorr.divide(pascalToInchOfHg, mathContext)

    val mercuryToTorr: BigDecimal get() = pascalToTorr.divide(mmOfHgToPascal, mathContext)

    val gramForceToTorr: BigDecimal get() = pascalToTorr.divide(gramForceToPascal, mathContext)

    val kgForceToTorr: BigDecimal get() = pascalToTorr.divide(kgForceToPascal, mathContext)

    val psiToTorr: BigDecimal get() = pascalToTorr.divide(pascalToPsi, mathContext)

    fun barToPascalConversions() =
        buildSparseIntArray(8) {
            append(21, 5)
            append(22, 5 + 9)//giga
            append(23, 6 + 5)//mega
            append(24, 3 + 5)//kilo
            append(25, 2 + 5)//hecto
            append(26, 1 + 5)//deca
            append(27, -1 + 5)//deci
            append(28, -2 + 5)//centi
            append(29, -3 + 5)//milli
        }

    fun mercuryConversions(): SparseIntArray =
        buildSparseIntArray(3) {
            append(18, -1)
            append(19, 0)
            append(20, 3)
        }

    fun pascalConversions(): SparseIntArray =
        Mass.buildPrefixMass()

    private inline fun <T> lazy(crossinline block: () -> T): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) {
            block()
        }
}