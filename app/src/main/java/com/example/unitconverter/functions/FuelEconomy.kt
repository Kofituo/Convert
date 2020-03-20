package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.FuelEconomy
import com.example.unitconverter.subclasses.Positions

class FuelEconomy(override val positions: Positions) : ConstantsAbstractClass() {
    override fun getText(): String =
        amongMetreLitre() ?: metrePerLitreConversions() ?: milePerLitreConversions()
        ?: footPerLitreConversions() ?: inchPerLitreConversions() ?: usGallonConversions()
        ?: amongMetreUsGallon() ?: amongMetreUkGallon() ?: ""

    private fun amongMetreLitre(): String? {
        if (topPosition in 0..4 && bottomPosition in 0..4)
            sparseIntArray(0) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun amongMetreUsGallon(): String? {
        if (topPosition in 10..14 && bottomPosition in 10..14)
            sparseIntArray(10) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun amongMetreUkGallon(): String? {
        if (topPosition in 15..19 && bottomPosition in 15..19)
            sparseIntArray(15) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun multipleMetreLitrePrefix(): Int {
        //to prevent double calling
        sparseIntArray(0) {
            val temp = it[topPosition, -200]
            //which one is not metre Litre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (topPosition > bottomPosition) 1 else -1
    }

    private fun metrePerLitreConversions(): String? {
        if (topPosition in 0..4 || bottomPosition in 0..4)
            fuel {
                ratio = when {
                    topPosition == 5 || bottomPosition == 5 -> {
                        //to mile per litre
                        metreToMileLitre
                    }
                    topPosition == 6 || bottomPosition == 6 -> {
                        //to foot per litre
                        metreToFootLitre
                    }
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        metreToInchLitre
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to miles per us gallon
                        miPerUsGalToMPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to miles per uk gallon
                        miPerUkGalToMPL
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(multipleMetreLitrePrefix())
            }
        return null
    }

    private fun milePerLitreConversions(): String? {
        if (topPosition == 5 || bottomPosition == 5)
            fuel {
                ratio = when {
                    topPosition == 6 || bottomPosition == 6 -> {
                        //to foot per litre
                        mileToFootLitre
                    }
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        mileToInchLiter
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per gallon
                        litreToGallonMile
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        litreToUkGallonMile
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun footPerLitreConversions(): String? {
        if (topPosition == 6 || bottomPosition == 6)
            fuel {
                ratio = when {
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        footToInchLitre
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per gallon
                        miPerGalToFPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        miPerUkGalToFPL
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun inchPerLitreConversions(): String? {
        if (topPosition == 7 || bottomPosition == 7)
            fuel {
                ratio = when {
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per us gallon
                        miPerGalToIPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        miPerUKGalToIPL
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun usGallonConversions(): String? {
        if (topPosition == 8 || bottomPosition == 8)
            fuel {
                ratio = when {
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per imp gallon
                        usGalToUkGalMile
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private inline fun fuel(block: FuelEconomy.() -> Unit) = FuelEconomy.apply(block)

    private inline fun sparseIntArray(start: Int, block: (SparseIntArray) -> Unit) =
        FuelEconomy.sparseIntArray(start).also(block)
}