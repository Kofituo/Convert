package com.otuolabs.unitconverter.constants

import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import java.math.BigDecimal

object Power {

    val amongWatt
        get() = Mass.buildPrefixMass().apply { append(size(), -7) }

    val wattToMetricHorsePower get() = BigDecimal("735.49875")

    private val wattToElectricHorsePower get() = BigDecimal(746)

    private val wattToImpHorsePower get() = BigDecimal("745.69987158227022")

    val metricToImpHp: BigDecimal
        get() = wattToImpHorsePower.divide(wattToMetricHorsePower, mathContext)

    val metricToImpHpInv: BigDecimal
        get() = wattToMetricHorsePower.divide(wattToImpHorsePower, mathContext)

    val metricToElectricHp: BigDecimal
        get() = wattToElectricHorsePower.divide(wattToMetricHorsePower, mathContext)

    val metricToElectricHpInv: BigDecimal
        get() = wattToMetricHorsePower.divide(wattToElectricHorsePower, mathContext)

    val impToElectricHp: BigDecimal
        get() = wattToElectricHorsePower.divide(wattToImpHorsePower, mathContext)

    val wattToCalorie: BigDecimal get() = Energy.jouleToCalorie.scaleByPowerOfTen(-3)

    val horsepowerToCalorie: BigDecimal
        get() = wattToCalorie.divide(wattToMetricHorsePower, mathContext)

    val footPoundToWatt get() = BigDecimal("1.35581794833140040")

    val footPoundToHorsepower: BigDecimal
        get() = footPoundToWatt.divide(wattToMetricHorsePower, mathContext)

    val footPoundToCalorie: BigDecimal
        get() = footPoundToWatt.divide(wattToCalorie, mathContext)

    val wattToBTU get() = Energy.jouleToThermalUnit

    val horsepowerToBTU: BigDecimal
        get() = wattToBTU.divide(wattToMetricHorsePower, mathContext)

    val calorieToBTU: BigDecimal
        get() = wattToBTU.divide(wattToCalorie, mathContext)

    val footPoundToBTU: BigDecimal
        get() = wattToBTU.divide(footPoundToWatt, mathContext)
}