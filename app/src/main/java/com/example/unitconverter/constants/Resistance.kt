package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.constants.BigDecimalsAddOns.speedOfLight
import java.math.BigDecimal

object Resistance {

    inline fun amongOhm(block: (SparseIntArray) -> Unit) =
        Mass.buildPrefixMass().apply { append(size(), -9) }.let(block)

    val statohmToOhms: BigDecimal
        get() = speedOfLight.scaleByPowerOfTen(2).pow(2).scaleByPowerOfTen(-9)
}