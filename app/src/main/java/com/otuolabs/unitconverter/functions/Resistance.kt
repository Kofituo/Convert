package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Resistance.amongOhm
import com.otuolabs.unitconverter.constants.Resistance.statohmToOhms
import com.otuolabs.unitconverter.subclasses.Positions

class Resistance(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText() = amongOhm() ?: ohmConversion() ?: TODO()

    private fun amongOhm(): String? {
        rangeAssertAnd(0..17) {
            amongOhm {
                return innerAmongPrefix(it)
            }
        }
        return null
    }

    private fun ohmConversion(): String? {
        rangeAssertOr(0..17) {
            intAssertOr(18) {
                ratio = statohmToOhms
                amongOhm {
                    innerMultiplePrefix(it)
                    return forMultiplePrefixes(swapConversions())
                }
            }
        }
        return null
    }
}