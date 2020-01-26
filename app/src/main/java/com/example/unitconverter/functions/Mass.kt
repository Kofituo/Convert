package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity.Positions
import com.example.unitconverter.constants.Mass

class Mass(positions: Positions) : ConstantsAbstractClass() {
    override val topPosition = positions.topPosition

    override val bottomPosition = positions.bottomPosition

    override val inputString = positions.input


    override fun getText(): String {
        return amongGram() ?: poundConversions()
        ?: gramConversions() ?: ounceConversions()
        ?: metricTonConversions() ?: shortTonConversions()
        ?: longTonConversions() ?: caratConversions()
        ?: grainConversions() ?: troyPoundConversion()
        ?: troyOunceConversions() ?: pennyWeightConversions()
        ?: ""
    }

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
                    constant = gramToOunceConstant
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
                    constant = shortTonToKgConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //gram to long Ton
                    constant = gramToLonTonConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //gram to carat
                    constant = gramToCaratConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = gramToTroyPoundConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToGramConstant
                    pow = simplifyKgConversions()
                    return forMultiplePrefixes(inputString, pow)
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToGramConstant
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
                    constant = gramToPoundConstant
                    return forMultiplePrefixes(inputString, simplifyKgConversions())
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    // pound to ounce
                    constant = poundToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //pound to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    constant = gramToPoundConstant.scaleByPowerOfTen(-3)
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //pound to short ton
                    constant = shortTonToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //pound to long ton
                    constant = poundToLonTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //pound to carat
                    constant = poundToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = troyPoundToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToPoundConstant
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
                    constant = shortTonToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    constant = metricTonToLonTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //metric ton to carat
                    constant = metricTonToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to grain
                    constant = grainToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = metricTonTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToMetricTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToMetricTonConstant
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
                    constant = gramToOunceConstant.scaleByPowerOfTen(-3)
                    return basicFunction(inputString, -swapConversions())
                }
                if (bottomPosition == 20 || topPosition == 20) {
                    //ounce to short ton
                    constant = ounceToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //ounce to long ton
                    constant = ounceToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //ounce to carat
                    constant = ounceToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToOunceConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy Pound
                    constant = troyPoundToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToOunceConstant
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
                    constant = shortTonToLongConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    constant = shortTonToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = shortTonToTroyPound
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToShortTonConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyweight
                    constant = pennyWeightToShortTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToShortTonConstant
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
                    constant = longTonToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToLongTonConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to troy pound
                    constant = longTonToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToLongTonConstant

                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToLongTonConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToLonTonConstant
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
                    constant = grainToCaratConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = caratToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = caratToTroyOunceConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToCaratConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToCaratConstant
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
                    constant = grainToTroyPoundConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToGrainConstant
                    return basicFunction(inputString, swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to stone
                    constant = stoneToGrainConstant
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
                    constant = troyOunceToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToTroyPoundConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToTroyPoundConstant
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
                    constant = pennyWeightToTroyOunceConstant
                    return basicFunction(inputString, -swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToTroyOunceConstant
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
                    constant = stoneToPennyWeightConstant
                    return basicFunction(inputString, swapConversions())
                }
            }
        }
        return null
    }


}