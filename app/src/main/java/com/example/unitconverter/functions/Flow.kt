package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns
import com.example.unitconverter.constants.Flow.flowMap
import com.example.unitconverter.subclasses.Positions

class Flow(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = flowMap.run {
        val topRatio = getValue(topPosition)
        val bottomRatio = getValue(bottomPosition)
        ratio = bottomRatio.divide(topRatio, BigDecimalsAddOns.mathContext)
        basicFunction(1)
    }
}