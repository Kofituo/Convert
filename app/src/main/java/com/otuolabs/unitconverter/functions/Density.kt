package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.BigDecimalsAddOns
import com.otuolabs.unitconverter.constants.Density
import com.otuolabs.unitconverter.subclasses.Positions

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