package com.example.unitconverter.constants

import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import java.math.BigDecimal

object Power {

    val amongWatt
        get() = Mass.buildPrefixMass().apply { append(size(), -7) }

    val wattToMetricHorsePower get() = BigDecimal("735.49875")

    private val wattToElectricHorsePower get() = BigDecimal(746)

    private val wattToImpHorsePower get() = BigDecimal("745.69987158227022")

    val metricToImpHp: BigDecimal
        get() = wattToImpHorsePower.divide(wattToMetricHorsePower, mathContext)

    val metricToElectricHp: BigDecimal
        get() = wattToElectricHorsePower.divide(wattToMetricHorsePower, mathContext)

    val impToElectricHp: BigDecimal
        get() = wattToElectricHorsePower.divide(wattToImpHorsePower, mathContext)

    val wattToCalorie: BigDecimal get() = Energy.jouleToCalorie.scaleByPowerOfTen(-3)

    val horsepowerToCalorie: BigDecimal
        get() = wattToCalorie.divide(wattToMetricHorsePower, mathContext)

    val footPoundToWatt get() = BigDecimal("1.35581794833140040")
}