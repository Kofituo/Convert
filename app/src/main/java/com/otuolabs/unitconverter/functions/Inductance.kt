package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.ElectricCurrent
import com.otuolabs.unitconverter.subclasses.Positions

class Inductance(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..6) { ElectricCurrent.amongAmp().apply { append(size(), -9) } } ?: TODO()
}