package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Illuminance.amongLux
import com.otuolabs.unitconverter.constants.Illuminance.illuminance
import com.otuolabs.unitconverter.constants.Illuminance.inchToFoot
import com.otuolabs.unitconverter.subclasses.Positions

class Illuminance(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..8) { amongLux } ?: luxConversions() ?: footCandleConversion()
        ?: TODO()

    /*private fun amongLux(): String? {
        if (rangeAssertAnd(0..8))
            amongLux {
                return innerAmongPrefix(it)
            }
        return null
    }*/

    private fun luxMultiplePrefix(): Int {
        //amongLux {
        innerMultiplePrefix(amongLux)
        //}
        return swapConversions()
    }

    private fun luxConversions(): String? {
        if (rangeAssertOr(0..8))
            illuminance {
                ratio = when {
                    intAssertOr(9) -> {
                        //to foot candle
                        luxToFootCandle
                    }
                    intAssertOr(10) -> {
                        //to lumen per square inch
                        luxToLumenPerSqInch
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(luxMultiplePrefix())
            }
        return null
    }

    private fun footCandleConversion(): String? {
        if (intAssertOr(9))
            if (intAssertOr(10)) {
                ratio = inchToFoot
                return result
            }
        return null
    }
}