package com.example.unitconverter.functions

import com.example.unitconverter.constants.Length
import com.example.unitconverter.subclasses.Positions

class Length(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongMetre() ?: metreConversions() ?: footConversions() ?: inchConversions()
        ?: mileConversions() ?: yardConversions() ?: nauticalMileConversions()
        ?: nauticalLeagueConversions() ?: fathomConversions() ?: rodConversions()
        ?: thouConversions() ?: chainConversions() ?: furlongConversions() ?: angstromConversions()
        ?: planckConversion()
        ?: throw TODO("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

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
            val temp = it[topPosition, -200]
            //which one is not metre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
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
                } else if (topPosition == 18 || bottomPosition == 18) {
                    //to inch
                    ratio = inchToMetre
                } else if (topPosition == 19 || bottomPosition == 19) {
                    //to mie
                    ratio = metreToMile
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = metresToYard
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = metreToNauticalMile
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToMetre
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToMetre
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToMetre
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToMetre
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chan
                    ratio = chainToMetre
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToMetre
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToMetre
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    ratio = planckLengthToMetre
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToMetre
                } else TODO()

                val pow = simplifyMultiplePrefix()
                return forMultiplePrefixes(pow)
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to mile
                    ratio = footToMile
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = yardToFeet
                    return basicFunction(-swapConversions())
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToFoot
                } else if (topPosition == 22 || bottomPosition == 2) {
                    //to nautical league
                    ratio = nauticalLeagueToFoot
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToFeet
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToFeet
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToFoot
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chan
                    ratio = chainToFeet
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToFeet
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToFoot
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToFoot
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToFoot
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = inchToYard
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToInch
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToInch
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToInch
                } else if (topPosition == 24 || bottomPosition == 24) {
                    // to rod
                    ratio = rodToInch
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToInch
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToInch
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToInch
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToInch
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToInch
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToInch
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToMile
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToMile
                } else if (topPosition == 23 || bottomPosition == 23) {
                    // to fathom
                    ratio = fathomToMile
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToMile
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToMile
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToMile
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToMile
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToMile
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToMile
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToMile
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to nautical league
                    ratio = nauticalLeagueToYard
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to fathom
                    ratio = fathomToYard
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToYard
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToYard
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToYard
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToYard
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToYard
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToYard
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToYard
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 23 || bottomPosition == 23) {
                    // to fathom
                    ratio = fathomToNauticalMile
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to rod
                    ratio = rodToNauticalMile
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToNauticalMile
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToNauticalMile
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToNauticalMile
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToNauticalMile
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToNauticalMile
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToNauticalMile
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 24 || bottomPosition == 24) {
                    // to rod
                    ratio = rodToLeague
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToNauticalLeague
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToNauticalLeague
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToNauticalLeague
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToNauticalLeague
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToLeague
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToLeague
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to thou
                    ratio = thouToFathom
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToFathom
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToFathom
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToFathom
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToFathom
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToFathom
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to chain
                    ratio = chainToRod
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToRod
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToRod
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToRod
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToRod
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    ratio = furlongToThou
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    ratio = angstromToThou
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    ratio = planckLengthToThou
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    ratio = lyToThou
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun chainConversions(): String? {
        if (topPosition == 26 || bottomPosition == 26) {
            Length.apply {
                ratio = if (topPosition == 27 || bottomPosition == 27) {
                    //to furlong
                    furlongToChain
                } else if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    chainToAngstrom
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    chainToPlanckLength
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    lyToChain
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun furlongConversions(): String? {
        if (topPosition == 27 || bottomPosition == 27) {
            Length.apply {
                ratio = if (topPosition == 28 || bottomPosition == 28) {
                    //to angstrom
                    furlongToAngstrom
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to planck length
                    furlongPlanckLength
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    lyToFurlong
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun angstromConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28) {
            Length.apply {
                ratio = if (topPosition == 29 || bottomPosition == 29) {
                    //to planckLength
                    planckLengthToAngstrom
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to light year
                    lyToAngstrom
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun planckConversion(): String? {
        if (topPosition == 29 || bottomPosition == 29) {
            if (topPosition == 30 || bottomPosition == 30) {
                //to light year
                ratio = Length.lyToPlanck
                return basicFunction(swapConversions())
            }
        }
        return null
    }
}