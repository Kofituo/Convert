package com.example.unitconverter.functions

import com.example.unitconverter.constants.Prefixes
import com.example.unitconverter.subclasses.Positions

class Prefixes(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        val sparseIntArray = Prefixes.buildPrefix()
        Prefixes.top = sparseIntArray[topPosition]
        Prefixes.bottom = sparseIntArray[bottomPosition]
        return Prefixes.prefixMultiplication(inputString)
    }
}