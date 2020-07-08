package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.ElectricCurrent
import com.otuolabs.unitconverter.subclasses.Positions

class ElectricCurrent(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..5) { ElectricCurrent.amongAmp() } ?: ampConversion() ?: TODO()

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