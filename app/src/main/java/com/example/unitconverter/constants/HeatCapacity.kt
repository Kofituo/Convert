package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildSparseIntArray

object HeatCapacity {

    inline fun heatCapacity(block: HeatCapacity.() -> Unit) = apply(block)

    val amongJoule
        get() =
            buildSparseIntArray(2) {
                append(0, 0)
                append(1, 3)//kilo
                append(2, 6)//mega //ton
                append(3, 9)//giga //kilo ton
            }

    val jouleToCalorie get() = Energy.jouleToCalorie

    val jouleToBtu get() = Energy.jouleToThermalUnit

}
