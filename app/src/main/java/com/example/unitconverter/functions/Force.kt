package com.example.unitconverter.functions

import com.example.unitconverter.constants.Force
import com.example.unitconverter.subclasses.Positions

class Force(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String = amongPrefixes(0..17, Force.amongNewton) ?: ""
}