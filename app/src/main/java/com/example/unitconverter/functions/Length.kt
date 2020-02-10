package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Length

class Length(override val positions: ConvertActivity.Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongMetre() ?: metreConversions() ?: footConversions() ?: inchConversions()
        ?: mileConversions() ?: yardConversions() ?: nauticalMileConversions()
        ?: nauticalLeagueConversions() ?: fathomConversions() ?: rodConversions()
        ?: thouConversions() ?: chainConversions() ?: furlongConversions() ?: angstromConversions()
        ?: planckConversion()
        ?: throw Exception("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

    private fun amongMetre(): String? {
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Length.metreConversions().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun simplifyMultiplePrefix(): Int {
        Length.metreConversions().also {
            val temp = it[topPosition, -2]
            //which one is not metre??
            val whichOne =
                if (temp == -2) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun metreConversions(): String? {
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Length.apply {
                if (topPosition == 17 || bottomPosition == 17) {
                    //to foot
                    ratio = footToMetre
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to inch
                    ratio = inchToMetre
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to mie
                    ratio = metreToMile
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = metresToYard
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = metreToNauticalMile
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToMetre
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToMetre
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToMetre
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToMetre
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chan
                    ratio = chainToMetre
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToMetre
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToMetre
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    ratio = planckLengthToMetre
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToMetre
                }
                val pow = simplifyMultiplePrefix()
                return forMultiplePrefixes(inputString, pow)
            }
        }
        return null
    }

    private fun footConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Length.apply {
                if (topPosition == 18 || bottomPosition == 18) {
                    //to inch
                    ratio = footToInch
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to mile
                    ratio = footToMile
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = yardToFeet
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToFoot
                }
                if (topPosition == 22 || bottomPosition == 2) {
                    //to nautical league
                    ratio = nauticalLeagueToFoot
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToFeet
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToFeet
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToFoot
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chan
                    ratio = chainToFeet
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToFeet
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToFoot
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToFoot
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToFoot
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun inchConversions(): String? {
        if (topPosition == 18 || bottomPosition == 18) {
            Length.apply {
                if (topPosition == 19 || bottomPosition == 19) {
                    //to mile
                    ratio = inchToMile
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = inchToYard
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToInch
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToInch
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToInch
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to rod
                    ratio = rodToInch
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToInch
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToInch
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToInch
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToInch
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToInch
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToInch
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun mileConversions(): String? {
        if (topPosition == 19 || bottomPosition == 19) {
            Length.apply {
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = mileToYard
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToMile
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToMile
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to fathom
                    ratio = fathomToMile
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToMile
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToMile
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToMile
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToMile
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToMile
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToMile
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToMile
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun yardConversions(): String? {
        if (topPosition == 20 || bottomPosition == 20) {
            Length.apply {
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToYard
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToYard
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToYard
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToYard
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToYard
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToYard
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToYard
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToYard
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToYard
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToYard
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun nauticalMileConversions(): String? {
        if (topPosition == 21 || bottomPosition == 21) {
            Length.apply {
                if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToNauticalMile
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to fathom
                    ratio = fathomToNauticalMile
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToNauticalMile
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToNauticalMile
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToNauticalMile
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToNauticalMile
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToNauticalMile
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToNauticalMile
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToNauticalMile
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun nauticalLeagueConversions(): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Length.apply {
                if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToNauticalLeague
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to rod
                    ratio = rodToLeague
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToNauticalLeague
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToNauticalLeague
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToNauticalLeague
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToNauticalLeague
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToLeague
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToLeague
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun fathomConversions(): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Length.apply {
                if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToFathom
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToFathom
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToFathom
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToFathom
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToFathom
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToFathom
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToFathom
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun rodConversions(): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Length.apply {
                if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToRod
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToRod
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToRod
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToRod
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToRod
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToRod
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun thouConversions(): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Length.apply {
                if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToThou
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToThou
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToThou
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToThou
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToThou
                }
            }
            return basicFunction(inputString, swapConversions())
        }
        return null
    }

    private fun chainConversions(): String? {
        if (topPosition == 26 || bottomPosition == 26) {
            Length.apply {
                if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToChain
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = chainToAngstrom
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    ratio = chainToPlanckLength
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToChain
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun furlongConversions(): String? {
        if (topPosition == 27 || bottomPosition == 27) {
            Length.apply {
                if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = furlongToAngstrom
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    ratio = furlongPlanckLength
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToFurlong
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun angstromConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28) {
            Length.apply {
                if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToAngstrom
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToAngstrom
                }
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }

    private fun planckConversion(): String? {
        if (topPosition == 29 || bottomPosition == 29) {
            if (topPosition == 30 || bottomPosition == 30) {
                //to light year
                ratio = Length.lyToPlanck
                return basicFunction(inputString, swapConversions())
            }
        }
        return null
    }
}