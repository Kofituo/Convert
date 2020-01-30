package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Length

class Length(override val positions: ConvertActivity.Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongMetre() ?: metreConversions() ?: footConversions() ?: inchConversions()
        ?: mileConversions() ?: yardConversions()
        ?: ""

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
                val pow: Int
                if (topPosition == 17 || bottomPosition == 17) {
                    //to foot
                    pow = simplifyMultiplePrefix()
                    ratio = footToMetre
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    //to inch
                    pow = simplifyMultiplePrefix()
                    ratio = inchToMetre
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //to mie
                    ratio = metreToMile
                    pow = simplifyMultiplePrefix()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = metresToYard
                    pow = simplifyMultiplePrefix()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = metreToNauticalMile
                    pow = simplifyMultiplePrefix()
                    return forMultiplePrefixes(inputString, pow)
                }
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
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = yardToFeet
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToFoot
                    return basicFunction(inputString, swapConversions())
                }
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
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //to yard
                    ratio = inchToYard
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToInch
                    return basicFunction(inputString, swapConversions())
                }
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
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //to nautical mile
                    ratio = nauticalMileToMile
                    return basicFunction(inputString, swapConversions())
                }
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
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }
}