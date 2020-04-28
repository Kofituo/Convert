package com.example.unitconverter.functions

import androidx.constraintlayout.solver.widgets.HelperWidget
import com.example.unitconverter.constants.HeatCapacity
import com.example.unitconverter.subclasses.Positions

class HeatCapacity(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = amongJoule() ?: jouleConversions()
    ?: ""

    private fun amongJoule(): String? {
        rangeAssertAnd(0..3) {
            HeatCapacity.amongJoule {
                return innerAmongPrefix(it)
            }
        }
        return null
    }

    private fun joulePrefixes(): Int {
        HeatCapacity.amongJoule {
            innerMultiplePrefix(it)
        }
        return swapConversions()
    }

    private fun jouleConversions(): String? {
        rangeAssertOr(0..3) {
            HeatCapacity.heatCapacity {
                ratio = when {
                    rangeAssertOr(4..5) -> {
                        jouleToCalorie
                    }
                    intAssertOr(7) -> {
                        jouleToBtu
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(joulePrefixes())
            }
        }
        return null
    }
}