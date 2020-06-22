package com.example.unitconverter.functions

import com.example.unitconverter.constants.Density
import com.example.unitconverter.subclasses.Positions

class Density(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..14, Density.amongGramPerMetre) ?: ""
}