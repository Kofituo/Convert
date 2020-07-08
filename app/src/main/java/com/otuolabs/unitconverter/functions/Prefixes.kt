package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Prefixes
import com.otuolabs.unitconverter.subclasses.Positions

class Prefixes(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        val sparseIntArray = Prefixes.buildPrefix()
        Prefixes.top = sparseIntArray[topPosition]
        Prefixes.bottom = sparseIntArray[bottomPosition]
        return Prefixes.prefixMultiplication(inputString)
    }
}