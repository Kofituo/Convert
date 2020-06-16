package com.example.unitconverter.functions

import com.example.unitconverter.constants.Resistance.amongOhm
import com.example.unitconverter.constants.Resistance.statohmToOhms
import com.example.unitconverter.subclasses.Positions

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