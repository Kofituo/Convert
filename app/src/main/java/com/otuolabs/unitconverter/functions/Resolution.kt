package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.builders.buildArrayMap
import com.otuolabs.unitconverter.builders.put
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns
import com.otuolabs.unitconverter.constants.Length
import com.otuolabs.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Resolution(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        buildArrayMap<Int, BigDecimal>(4) {
            put {
                key = 0
                value = BigDecimal.ONE
            }
            put {
                key = 1
                value = Length.inchToMetre
            }
            put {
                key = 2
                value = Length.inchToMetre.scaleByPowerOfTen(2)
            }
            put {
                key = 3
                value = Length.inchToMetre.scaleByPowerOfTen(3)
            }
        }.run {
            val topRatio = getValue(topPosition)
            val bottomRatio = getValue(bottomPosition)
            ratio = topRatio.divide(bottomRatio, BigDecimalsAddOns.mathContext)
            basicFunction(1)
        }
}