package com.example.unitconverter.functions

import com.example.unitconverter.constants.ElectricCurrent
import com.example.unitconverter.subclasses.Positions

class ElectricCurrent(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = amongAmp() ?: ampConversion() ?: TODO()

    private fun amongAmp(): String? {
        if (rangeAssertAnd(0..5))
            current {
                return innerAmongPrefix(amongAmp())
            }
        return null
    }


    private fun ampPrefix(): Int {
        current {
            innerMultiplePrefix(amongAmp())
        }
        return swapConversions()
    }

    private fun ampConversion(): String? {
        if (rangeAssertOr(0..5))
            current {
                if (intAssertOr(6)) {
                    //to ab ampere
                    ratio = ampToBiot
                    return forMultiplePrefixes(ampPrefix())
                }
            }
        return null
    }

    private inline fun current(block: ElectricCurrent.() -> Unit) = ElectricCurrent.apply(block)
}