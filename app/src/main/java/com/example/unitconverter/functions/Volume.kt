package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Volume

class Volume(override val positions: ConvertActivity.Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText(): String =
        amongSquareMetre() ?: metreToLitre() ?: ""


    private fun amongSquareMetre(): String? {
        if (topPosition in 0..7 && bottomPosition in 0..7) {
            Volume.amongCubicMetreMap().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun metreToLitre(): String? {
        if ((topPosition in 0..7 || bottomPosition in 0..7) &&
            (topPosition in 8..15 || bottomPosition in 8..15)
        ) {
            with(Volume) {
                amongCubicMetreMap().apply {
                    metreToLitreMap().also {
                        val somethingLitre =
                            if (it[topPosition, 0] == 0) it[bottomPosition] else it[topPosition]
                        val somethingMetre =
                            if (get(topPosition, 100) == 100)
                                get(bottomPosition)
                            else get(topPosition)
                        if (swapConversions() == 1) {
                            top = somethingLitre
                            bottom = somethingMetre
                        } else {
                            top = somethingMetre
                            bottom = somethingLitre
                        }
                        return prefixMultiplication(inputString)
                    }
                }
            }
        }
        return null
    }

    private fun metreConverions(): String? {
        if (topPosition in 0..7 || bottomPosition in 0..7) {
            Volume.apply {
                if (topPosition == 16 || bottomPosition == 16) {

                }
            }
        }
        return null
    }
}