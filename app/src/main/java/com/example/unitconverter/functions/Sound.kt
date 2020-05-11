package com.example.unitconverter.functions

import com.example.unitconverter.constants.Sound
import com.example.unitconverter.subclasses.Positions

class Sound(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = amongBel() ?: belToNeper() ?: TODO()

    private fun amongBel(): String? {
        rangeAssertAnd(0..2) {
            Sound.amongBel {
                return innerAmongPrefix(it)
            }
        }
        return null
    }

    private fun belPrefixes(): Int {
        Sound.amongBel {
            innerMultiplePrefix(it)
        }
        return swapConversions()
    }

    private fun belToNeper(): String? {
        rangeAssertOr(0..2) {
            intAssertOr(3) {
                //to neper
                ratio = Sound.neperToBel
                return forMultiplePrefixes(belPrefixes())
            }
        }
        return null
    }
}