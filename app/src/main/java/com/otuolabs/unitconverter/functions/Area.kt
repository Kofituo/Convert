package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Area
import com.otuolabs.unitconverter.subclasses.Positions

class Area(override val positions: Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText(): String =
        amongSquareMetre() ?: metreConversions() ?: footConversions() ?: inchConversions()
        ?: yardConversions() ?: mileConversions() ?: nauticalMileConversions()
        ?: nauticalLeagueConversions() ?: chainConversions() ?: acreConversions()
        ?: hectareConversions() ?: areConversion()
        ?: throw TODO("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

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
            val temp = it[topPosition, -200]
            //which one is not squareMetre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
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
                } else if (topPosition == 9 || bottomPosition == 9) {
                    //to inch
                    ratio = metreToInch
                } else if (topPosition == 10 || bottomPosition == 10) {
                    //to yard
                    ratio = metreToYard
                } else if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = metreToMile
                } else if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = metreToNauticalMiles
                } else if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToMetre
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToMetres
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToMetre
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToMetre
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToMetre
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToMetre
                } else TODO()

                val pow = simplifyMultiplePrefix()
                return forMultiplePrefixes(pow)
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
                } else if (topPosition == 9 || bottomPosition == 9) {
                    //to inch
                    ratio = footToInch
                } else if (topPosition == 10 || bottomPosition == 10) {
                    //to yard
                    ratio = footToYard
                } else if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = feetToMile
                } else if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = feetToNauticalMile
                } else if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToFeet
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToFoot
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToFoot
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToFeet
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToFeet
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToFeet
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 11 || bottomPosition == 11) {
                    //to mile
                    ratio = mileToInch
                } else if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = inchesToNauticalMile
                } else if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToInch
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToInch
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToInch
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToInch
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToInch
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToInch
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 12 || bottomPosition == 12) {
                    //to nautical mile
                    ratio = yardToNauticalMile
                } else if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToYard
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToYards
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToYard
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToYard
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToYard
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToYard
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 13 || bottomPosition == 13) {
                    //to league
                    ratio = leagueToMile
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToMile
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToMile
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = mileToHectare
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToMile
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToMile
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 14 || bottomPosition == 14) {
                    //to chain
                    ratio = chainToNauticalMiles
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToNauticalMile
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToNauticalMile
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToNauticalMile
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToNauticalMile
                } else TODO()

                return basicFunction(pow)
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
                } else if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    ratio = acreToNauticalLeague
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    ratio = hectareToLeague
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    ratio = areToLeague
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    ratio = barnToLeague
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun chainConversions(): String? {
        if (topPosition == 14 || bottomPosition == 14) {
            Area.apply {
                ratio = if (topPosition == 15 || bottomPosition == 15) {
                    //to acre
                    chainToAcre
                } else if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    hectareToChain
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    areToChain
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    barnToChain
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun acreConversions(): String? {
        if (topPosition == 15 || bottomPosition == 15) {
            Area.apply {
                ratio = if (topPosition == 16 || bottomPosition == 16) {
                    //to hectare
                    acreToHectare
                } else if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    areToAcre
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    barnToAcre
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun hectareConversions(): String? {
        if (topPosition == 16 || bottomPosition == 16) {
            Area.apply {
                ratio = if (topPosition == 17 || bottomPosition == 17) {
                    //to are
                    areToHectare
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to barn
                    barnToHectare
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun areConversion(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            if (topPosition == 18 || bottomPosition == 18) {
                ratio = Area.barnToAre
                return basicFunction(pow)
            }
        }
        return null
    }
}