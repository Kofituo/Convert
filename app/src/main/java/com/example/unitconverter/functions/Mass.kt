package com.example.unitconverter.functions

import android.util.Log
import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Mass

class Mass(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText() =
        amongGram() ?: poundConversions()
        ?: gramConversions() ?: ounceConversions()
        ?: metricTonConversions() ?: shortTonConversions()
        ?: longTonConversions() ?: caratConversions()
        ?: grainConversions() ?: troyPoundConversion()
        ?: troyOunceConversions() ?: pennyWeightConversions()
        ?: stoneConversions() ?: slugConversions()
        ?: amuConversion() ?: plankMassConversion()
        ?: throw Exception("top position = $topPosition  bottom position = $bottomPosition")

    private fun amongGram(): String? {
        //val sparseArray = buildPrefixMass()
        // means its amongst the gram family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Mass.apply {
                buildPrefixMass().also {
                    top = it[topPosition]
                    bottom = it[bottomPosition]
                    return prefixMultiplication(inputString)
                }
            }
        }
        return null
    }

    private fun gramConversions(): String? {
        // among gram
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Mass.apply {
                val pow: Int
                if (topPosition == 18 || bottomPosition == 18) {
                    //gram to ounce (oz) or vice versa
                    ratio = gramToOunceConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //gram to metric ton
                    gramToMetricTonConversion()
                    return prefixMultiplication(inputString)
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //gram to short Ton
                    ratio = shortTonToKgConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //gram to long Ton
                    ratio = gramToLonTonConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //gram to carat
                    ratio = gramToCaratConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = gramToTroyPoundConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = gramToSlugConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToKg
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mas
                    ratio = planckMassToKg
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //final solar mass
                    ratio = solarMassToKg
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
            }
        }
        return null
    }

    private fun gramToMetricTonConversion() {
        Mass.apply {
            buildPrefixMass().also {
                val temp = it[topPosition, -2]
                val metricTonPosition = it[5]
                //
                val whichOne =
                    if (temp == -2) it[bottomPosition] else temp

                if (topPosition > bottomPosition) {
                    top = metricTonPosition
                    bottom = whichOne
                } else {
                    top = whichOne
                    bottom = metricTonPosition
                }
            }
        }
    }

    private fun simplifyKgConversions(): Int {
        //to prevent double calling
        Mass.buildPrefixMass().also {
            val temp = it[topPosition, -2]
            val kgPosition = it[6]
            //which one is not kilogram??
            val whichOne =
                if (temp == -2) it[bottomPosition] else temp

            top = whichOne
            bottom = kgPosition
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    //it works like a charm
    private fun poundConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Mass.apply {
                if (topPosition in 0..16 || bottomPosition in 0..16) {
                    // g to lb or vice versa
                    ratio = gramToPoundConstant
                    return forMultiplePrefixes(inputString, simplifyKgConversions())
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    // pound to ounce
                    ratio = poundToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //pound to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    ratio = gramToPoundConstant.scaleByPowerOfTen(-3)
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //pound to short ton
                    ratio = shortTonToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //pound to long ton
                    ratio = poundToLonTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //pound to carat
                    ratio = poundToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = troyPoundToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToPound
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mas
                    ratio = planckMassToPound
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToPound
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun metricTonConversions(): String? {
        if (topPosition == 19 || bottomPosition == 19) {
            Mass.apply {
                if (topPosition == 20 || bottomPosition == 20) {
                    //short ton to metric ton
                    ratio = shortTonToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    ratio = metricTonToLonTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //metric ton to carat
                    ratio = metricTonToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to grain
                    ratio = grainToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = metricTonTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = metricTonToSlugConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToMetricTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToMetricTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToMetricTon
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun ounceConversions(): String? {
        if (topPosition == 18 || bottomPosition == 18) {
            Mass.apply {
                if (topPosition == 19 || bottomPosition == 19) {
                    //ounce to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    ratio = gramToOunceConstant.scaleByPowerOfTen(-3)
                    return basicFunction(inputString, -swapConversions())
                }
                if (bottomPosition == 20 || topPosition == 20) {
                    //ounce to short ton
                    ratio = ounceToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //ounce to long ton
                    ratio = ounceToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //ounce to carat
                    ratio = ounceToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToOunceConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy Pound
                    ratio = troyPoundToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToOunce
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToOunce
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToOunce
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun shortTonConversions(): String? {
        if (topPosition == 20 || bottomPosition == 20) {
            Mass.apply {
                if (topPosition == 21 || bottomPosition == 21) {
                    //short Ton to long ton
                    ratio = shortTonToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    ratio = shortTonToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = shortTonToTroyPound
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToShortTonConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyweight
                    ratio = pennyWeightToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToShotTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = plankMassToShortTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    // to solar mass
                    ratio = solarMassToShortTon
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun longTonConversions(): String? {
        if (topPosition == 21 || bottomPosition == 21) {
            Mass.apply {
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    ratio = longTonToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToLongTonConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to troy pound
                    ratio = longTonToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToLongTonConstant

                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToLonTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToLongTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToLongTon
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    // to solar mass
                    ratio = solarMassToLongTon
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun caratConversions(): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Mass.apply {
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = caratToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = caratToTroyOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToCarat
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToCarat
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToCarat
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun grainConversions(): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Mass.apply {
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = grainToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to stone
                    ratio = stoneToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToGrain
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    ratio = planckMassToGrain
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToGrain
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun troyPoundConversion(): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Mass.apply {
                //troy pound
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToTroyPound
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToTroyPound
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToTroyPound
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun troyOunceConversions(): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Mass.apply {
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToTroyOunceConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToTroyOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToTroyOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToTroyOunce
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck
                    ratio = planckMassToTroyOunce
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToTroyOunce
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun pennyWeightConversions(): String? {
        if (topPosition == 26 || bottomPosition == 26) {
            Mass.apply {
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToPennyWeightConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToPennyWeight
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    ratio = amuToPennyWeight
                    return basicFunction(inputString, swapConversions())
                    //to amu
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToPennyWeight
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToPennyWeight
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun stoneConversions(): String? {
        if (topPosition == 27 || bottomPosition == 27) {
            Mass.apply {
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToStoneConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToStone
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToStone
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToStone
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun slugConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28) {
            Mass.apply {
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToSlug
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToSlug
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToSlug
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    private fun amuConversion(): String? {
        if (topPosition == 29 || bottomPosition == 29) {
            Mass.apply {
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToAmu
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToAmu
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }

    // last function  yaaaaaaaaaaaay
    private fun plankMassConversion(): String? {
        if (topPosition == 30 || bottomPosition == 30 &&
            topPosition == 31 || bottomPosition == 31
        ) {
            ratio = Mass.solarMassToPlanckMass
            Log.e("ratio", "$ratio  ${basicFunction(inputString, swapConversions())}")
            return basicFunction(inputString, swapConversions())
        }
        return null
    }
}