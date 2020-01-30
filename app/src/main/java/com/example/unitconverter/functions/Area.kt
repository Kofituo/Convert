package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Area

class Area(override val positions: Positions) : ConstantsAbstractClass() {

    private val pow get() = swapConversions()

    override fun getText(): String =
        amongSquareMetre()
            ?: ""

    private fun amongSquareMetre(): String? {
        if (topPosition in 0..7 || bottomPosition in 0..7) {
            Area.amongSquareMetreMap().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun footConversions(): String? {
        if (topPosition == 8 || bottomPosition == 8) {
            Area.apply {

            }
        }
        return null
    }
}