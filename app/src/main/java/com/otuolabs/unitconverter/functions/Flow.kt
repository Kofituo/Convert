package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.BigDecimalsAddOns
import com.otuolabs.unitconverter.constants.Flow.flowMap
import com.otuolabs.unitconverter.subclasses.Positions

class Flow(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = flowMap.run {
        val topRatio = getValue(topPosition)
        val bottomRatio = getValue(bottomPosition)
        ratio = bottomRatio.divide(topRatio, BigDecimalsAddOns.mathContext)
        basicFunction(1)
    }
}