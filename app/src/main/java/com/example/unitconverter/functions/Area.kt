package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Area

class Area(override val positions: Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText(): String =
        amongSquareMetre() ?: metreConversions() ?: footConversions() ?: inchConversions()
        ?: yardConversions() ?: mileConversions() ?: nauticalMileConversions()
        ?: nauticalLeagueConversions() ?: chainConversions() ?: acreConversions()
        ?: hectareConversions() ?: areConversion()
        ?: throw Exception("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

    private fun amongSquareMetre(): String? {
        if (topPosition in 0..7 && bottomPosition in 0..7) {
            Area.amongSquareMetreMap().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun simplifyMultiplePrefix(): Int {
        Area.amongSquareMetreMap().also {
            val temp = it[topPosition, -2]
            //which one is not squareMetre??
            val whichOne =
                if (temp == -2) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun metreConversions(): String? {
        if (topPosition in 0..7 || bottomPosition in 0..7) {
            Area.apply {
                if (topPosition == 8 || bottomPosition == 8) {
                    //to foot
                    ratio = feetToMetre
                }
                if (topPosition == 9 || bottomPosition == 9) {
                    //to inch
                    ratio = metreToInch
                }
                if (topPosition == 10 || bottomPosition == 10) {
                    //to yard
                    ratio = metreToYard
                }
                if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = metreToMile
                }
                if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = metreToNauticalMiles
                }
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToMetre
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToMetres
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToMetre
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToMetre
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToMetre
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToMetre
                }
                val pow = simplifyMultiplePrefix()
                return forMultiplePrefixes(inputString, pow)
            }
        }
        return null
    }

    private fun footConversions(): String? {
        if (topPosition == 8 || bottomPosition == 8) {
            Area.apply {
                if (topPosition == 9 || bottomPosition == 9) {
                    //to inch
                    ratio = footToInch
                }
                if (topPosition == 9 || bottomPosition == 9) {
                    //to inch
                    ratio = footToInch
                }
                if (topPosition == 10 || bottomPosition == 10) {
                    //to yard
                    ratio = footToYard
                }
                if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = feetToMile
                }
                if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = feetToNauticalMile
                }
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToFeet
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToFoot
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToFoot
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToFeet
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToFeet
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToFeet
                }

                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun inchConversions(): String? {
        if (topPosition == 9 || bottomPosition == 9) {
            Area.apply {
                if (topPosition == 10 || bottomPosition == 10) {
                    //to yard
                    ratio = inchToYard
                }
                if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = mileToInch
                }
                if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = inchesToNauticalMile
                }
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToInch
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToInch
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToInch
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToInch
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToInch
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToInch
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun yardConversions(): String? {
        if (topPosition == 10 || bottomPosition == 10) {
            Area.apply {
                if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = yardToMile
                }
                if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = yardToNauticalMile
                }
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToYard
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToYards
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToYard
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToYard
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToYard
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToYard
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun mileConversions(): String? {
        if (topPosition == 11 || bottomPosition == 11) {
            Area.apply {
                if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = milesToNauticalMile
                }
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToMile
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToMile
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToMile
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = mileToHectare
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToMile
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToMile
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun nauticalMileConversions(): String? {
        if (topPosition == 12 || bottomPosition == 12) {
            Area.apply {
                if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = nauticalMileToLeague
                }
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToNauticalMiles
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToNauticalMile
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToNauticalMile
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToNauticalMile
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToNauticalMile
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun nauticalLeagueConversions(): String? {
        if (topPosition == 13 || bottomPosition == 13) {
            Area.apply {
                if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToLeague
                }
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToNauticalLeague
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToLeague
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToLeague
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToLeague
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun chainConversions(): String? {
        if (topPosition == 14 || bottomPosition == 14) {
            Area.apply {
                if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = chainToAcre
                }
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToChain
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToChain
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToChain
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun acreConversions(): String? {
        if (topPosition == 15 || bottomPosition == 15) {
            Area.apply {
                if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = acreToHectare
                }
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToAcre
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToAcre
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun hectareConversions(): String? {
        if (topPosition == 16 || bottomPosition == 16) {
            Area.apply {
                if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToHectare
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToHectare
                }
                return basicFunction(inputString, pow)
            }
        }
        return null
    }

    private fun areConversion(): String? {
        if (topPosition == 17 || bottomPosition == 17 &&
            topPosition == 18 || bottomPosition == 18
        ) {
            ratio = Area.barnToAre
            return basicFunction(inputString, pow)
        }
        return null
    }
}