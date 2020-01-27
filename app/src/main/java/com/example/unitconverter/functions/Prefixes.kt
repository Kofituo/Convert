package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Prefixes

class Prefixes(override val positions: ConvertActivity.Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        val sparseIntArray = Prefixes.buildPrefix()
        Prefixes.top = sparseIntArray[topPosition]
        Prefixes.bottom = sparseIntArray[bottomPosition]
        return Prefixes.prefixMultiplication(inputString)
    }
}