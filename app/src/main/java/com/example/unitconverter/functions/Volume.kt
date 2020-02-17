package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Volume

class Volume(override val positions: ConvertActivity.Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText(): String =
        amongSquareMetre() ?: metreToLitre() ?: amongLitre() ?: metreLitreConversions()
        ?: inchConversions() ?: footConversions() ?: yardConversions() ?: usGallonConversions()
        ?: impGallonConversions() ?: usPintConversions() ?: impPintConversions()
        ?: barrelConversions() ?: fluidOunceConversions() ?: impFluidOunceConversions()
        ?: quartConversion()
        ?: throw Exception("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one


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

    private fun simplifyMultiplePrefixMetre(): Int {
        Volume.amongCubicMetreMap().also {
            val temp = it[topPosition, -2]
            //which one is not cubicMetre??
            val whichOne =
                if (temp == -2) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun simplifyMultiplePrefixLitre(): Int {
        Volume.metreToLitreMap().also {
            val temp = it[topPosition, -20]
            //which one is not litre ??
            val whichOne =
                if (temp == -20) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
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

    private fun amongLitre(): String? {
        if (topPosition in 8..15 && bottomPosition in 8..15) {
            Volume.metreToLitreMap().also {
                top = it[topPosition] + 3
                bottom = it[bottomPosition] + 3
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun metreLitreConversions(): String? {
        if (topPosition in 0..15 || bottomPosition in 0..15) {
            Volume.apply {
                if (topPosition == 16 || bottomPosition == 16) {
                    //to inch
                    ratio = metreToInch
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to feet
                    ratio = metreToFoot
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to yard
                    ratio = metreToYard
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = metreToGallon
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = metreToImpGallon
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = metreToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = metreToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = metreToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = metreToFlOz
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = metreToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to quart
                    ratio = metreToQuart
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = metreToImpQuart
                }
                val pow =
                    if (topPosition in 0..7 || bottomPosition in 0..7)
                        simplifyMultiplePrefixMetre()
                    else simplifyMultiplePrefixLitre()

                return forMultiplePrefixes(inputString, pow)
            }
        }
        return null
    }

    private fun inchConversions(): String? {
        if (topPosition == 16 || bottomPosition == 16) {
            Volume.apply {
                if (topPosition == 17 || bottomPosition == 17) {
                    //to foot
                    ratio = footToInch
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to yard
                    ratio = inchToYard
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = inchToGallon
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = inchToImpGallon
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = inchToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = inchToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to oil barrel
                    ratio = inchToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = inchToFlOz
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = inchToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = inchToQuart
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = inchToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun footConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Volume.apply {
                if (topPosition == 18 || bottomPosition == 18) {
                    //to yard
                    ratio = feetToYard
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = feetToGallon
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = feetToImpGallon
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = footToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = feetToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to oil barrel
                    ratio = footToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = footToFlOz
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = footToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = footToQuart
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = footToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun yardConversions(): String? {
        if (topPosition == 18 || bottomPosition == 18) {
            Volume.apply {
                if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = yardToGallon
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = yardToImpGallon
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = yardToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = yardToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = yardToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = yardToFlOz
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = yardToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = yardToQuart
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = yardToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun usGallonConversions(): String? {
        if (topPosition == 19 || bottomPosition == 19) {
            Volume.apply {
                if (topPosition == 20 || bottomPosition == 20) {
                    //to imp gallon
                    ratio = gallonToImpGallon
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = gallonToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = gallonToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = gallonToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToGallon
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = gallonToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToGallon
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = gallonToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun impGallonConversions(): String? {
        if (topPosition == 20 || bottomPosition == 20) {
            Volume.apply {
                if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = impGallonToPint
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = impGallonToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = impGallonToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToImpGallon
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fluid ounce
                    ratio = impFluidOunceToImpGallon
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToImpGallon
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = impQuartToImpGallon
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun usPintConversions(): String? {
        if (topPosition == 21 || bottomPosition == 21) {
            Volume.apply {
                if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = pintToImpPint
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = usPintToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToPint
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = pintToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToPint
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = pintToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun impPintConversions(): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Volume.apply {
                if (topPosition == 23 || bottomPosition == 23) {
                    ratio = impPintToBarrel
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToImpPint
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = impFluidOunceToImpPint
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToImpPint
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = impQuartToImpPint
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun barrelConversions(): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Volume.apply {
                if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToBarrel
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = barrelToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to quart
                    ratio = quartToBarrel
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = barrelToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun fluidOunceConversions(): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Volume.apply {
                if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = flOzToImpFlOz
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToFlOz
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = flOzToImpQuart
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun impFluidOunceConversions(): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Volume.apply {
                if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToImpFlOz
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = impQuartToImpFlOz
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun quartConversion(): String? {
        if (topPosition == 26 || bottomPosition == 26) {
            Volume.apply {
                if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = quartToImpQuart
                    return basicFunction(inputString, pow)
                }
            }
        }
        return null
    }
}