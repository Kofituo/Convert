package com.example.unitconverter.functions

import com.example.unitconverter.constants.BigDecimalsAddOns
import com.example.unitconverter.constants.Density
import com.example.unitconverter.subclasses.Positions

class Density(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..14) { Density.amongGramPerMetre } ?: shortFun()

    private fun shortFun(): String {
        Density.densityMap.apply {
            val topRatio = getValue(topPosition)
            val bottomRatio = getValue(bottomPosition)
            ratio = topRatio.divide(bottomRatio, BigDecimalsAddOns.mathContext)
            return basicFunction(1)
        }
    }
}
/**
 * Example conversion
 * ounce to gram = 28.3g
 * us gal to cm3 = 3785.41
 * */