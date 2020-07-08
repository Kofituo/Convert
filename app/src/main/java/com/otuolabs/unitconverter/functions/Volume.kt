package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Volume
import com.otuolabs.unitconverter.subclasses.Positions

class Volume(override val positions: Positions) : ConstantsAbstractClass() {

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
            val temp = it[topPosition, -200]
            //which one is not cubicMetre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
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
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to feet
                    ratio = metreToFoot
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to yard
                    ratio = metreToYard
                } else if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = metreToGallon
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = metreToImpGallon
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = metreToPint
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = metreToImpPint
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = metreToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = metreToFlOz
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = metreToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to quart
                    ratio = metreToQuart
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = metreToImpQuart
                } else TODO()

                val pow =
                    if (topPosition in 0..7 || bottomPosition in 0..7)
                        simplifyMultiplePrefixMetre()
                    else simplifyMultiplePrefixLitre()

                return forMultiplePrefixes(pow)
            }
        }
        return null
    }

    private fun inchConversions(): String? {
        if (topPosition == 16 || bottomPosition == 16) {
            Volume.apply {
                when {
                    topPosition == 17 || bottomPosition == 17 -> {
                        //to foot
                        ratio = footToInch
                    }
                    topPosition == 18 || bottomPosition == 18 -> {
                        //to yard
                        ratio = inchToYard
                    }
                    topPosition == 19 || bottomPosition == 19 -> {
                        //to us gallon
                        ratio = inchToGallon
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to uk gallon
                        ratio = inchToImpGallon
                    }
                    topPosition == 21 || bottomPosition == 21 -> {
                        //to us pint
                        ratio = inchToPint
                    }
                    topPosition == 22 || bottomPosition == 22 -> {
                        //to imp pint
                        ratio = inchToImpPint
                    }
                    topPosition == 23 || bottomPosition == 23 -> {
                        //to oil barrel
                        ratio = inchToBarrel
                    }
                    topPosition == 24 || bottomPosition == 24 -> {
                        //to fl oz
                        ratio = inchToFlOz
                    }
                    topPosition == 25 || bottomPosition == 25 -> {
                        //to imp fl oz
                        ratio = inchToImpFlOz
                    }
                    topPosition == 26 || bottomPosition == 26 -> {
                        //to us quart
                        ratio = inchToQuart
                    }
                    topPosition == 27 || bottomPosition == 27 -> {
                        //to imp quart
                        ratio = inchToImpQuart
                    }
                    else -> TODO()
                }

                return basicFunction(pow)
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
                } else if (topPosition == 19 || bottomPosition == 19) {
                    //to us gallon
                    ratio = feetToGallon
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = feetToImpGallon
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = footToPint
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = feetToImpPint
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to oil barrel
                    ratio = footToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = footToFlOz
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = footToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = footToQuart
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = footToImpQuart
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to uk gallon
                    ratio = yardToImpGallon
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = yardToPint
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = yardToImpPint
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = yardToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = yardToFlOz
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = yardToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = yardToQuart
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = yardToImpQuart
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to us pint
                    ratio = gallonToPint
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = gallonToImpPint
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = gallonToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToGallon
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = gallonToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToGallon
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = gallonToImpQuart
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to imp pint
                    ratio = impGallonToImpPint
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = impGallonToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToImpGallon
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fluid ounce
                    ratio = impFluidOunceToImpGallon
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToImpGallon
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = impQuartToImpGallon
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to barrel
                    ratio = usPintToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    ratio = flOzToPint
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    ratio = pintToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    ratio = quartToPint
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    ratio = pintToImpQuart
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun impPintConversions(): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Volume.apply {
                ratio = if (topPosition == 23 || bottomPosition == 23) {
                    impPintToBarrel
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    flOzToImpPint
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    impFluidOunceToImpPint
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    quartToImpPint
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    impQuartToImpPint
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun barrelConversions(): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Volume.apply {
                ratio = if (topPosition == 24 || bottomPosition == 24) {
                    //to fl oz
                    flOzToBarrel
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    barrelToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to quart
                    quartToBarrel
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    barrelToImpQuart
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun fluidOunceConversions(): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Volume.apply {
                ratio = if (topPosition == 25 || bottomPosition == 25) {
                    //to imp fl oz
                    flOzToImpFlOz
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    quartToFlOz
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    flOzToImpQuart
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun impFluidOunceConversions(): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Volume.apply {
                ratio = if (topPosition == 26 || bottomPosition == 26) {
                    //to us quart
                    quartToImpFlOz
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to imp quart
                    impQuartToImpFlOz
                } else TODO()

                return basicFunction(pow)
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
                    return basicFunction(pow)
                }
            }
        }
        return null
    }
}