package com.example.unitconverter.functions

import com.example.unitconverter.constants.ElectricCurrent
import com.example.unitconverter.subclasses.Positions

class Inductance(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..6) { ElectricCurrent.amongAmp().apply { append(size(), -9) } } ?: TODO()
}