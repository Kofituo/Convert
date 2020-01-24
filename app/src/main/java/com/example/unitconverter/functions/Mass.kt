package com.example.unitconverter.functions

import com.example.unitconverter.ConvertActivity
import com.example.unitconverter.constants.Mass

class Mass(private val positions: ConvertActivity.Positions) : FunctionsInterface {
    override val topPosition get() = positions.topPosition

    override val bottomPosition get() = positions.bottomPosition

    override val inputString get() = positions.input

    override fun getText(): String {
        return amongGram(inputString) ?: poundConversions(inputString)
        ?: gramConversions(inputString) ?: ounceConversions(inputString)
        ?: metricTonConversions(inputString) ?: shortTonConversions(inputString)
        ?: longTonConversions(inputString) ?: caratConversions(inputString)
        ?: grainConversions(inputString) ?: troyPoundConversion(inputString)
        ?: troyOunceConversions(inputString)
        ?: ""
    }

    private fun amongGram(x: String): String? {
        //val sparseArray = buildPrefixMass()
        // means its amongst the gram family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Mass.apply {
                buildPrefixMass().also {
                    top = it[topPosition]
                    bottom = it[bottomPosition]
                    return prefixMultiplication(x)
                }
            }
        }
        return null
    }

    private fun gramConversions(x: String): String? {
        // among gram
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Mass.apply {
                val pow: Int
                if (topPosition == 18 || bottomPosition == 18) {
                    //gram to ounce (oz) or vice versa
                    constant = gramToOunceConstant
                    pow = simplifyKgConversions()
                    return gramToOunce(x, pow)
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //gram to metric ton
                    gramToMetricTonConversion()
                    return prefixMultiplication(x)
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //gram to short Ton
                    constant = shortTonToKgConstant
                    pow = simplifyKgConversions()
                    return gramToShortTon(x, pow)
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //gram to long Ton
                    constant = gramToLonTonConstant
                    pow = simplifyKgConversions()
                    return gramToLongTon(x, pow)
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //gram to carat
                    constant = gramToCaratConstant
                    pow = simplifyKgConversions()
                    return gramToCarat(x, pow)
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToGramConstant
                    pow = simplifyKgConversions()
                    return grainToGram(x, pow)
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = gramToTroyPoundConstant
                    pow = simplifyKgConversions()
                    return gramToTroyPound(x, pow)
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGramConstant
                    pow = simplifyKgConversions()
                    return troyOunceToGram(x, pow)
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToGramConstant
                    pow = simplifyKgConversions()
                    return pennyWeightToGram(x, pow)
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToGramConstant
                    pow = simplifyKgConversions()
                    return stoneToGram(x, pow)

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

            Mass.top = whichOne
            Mass.bottom = kgPosition
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun simplifyLbConversions() = if (topPosition > bottomPosition) 1 else -1
    //it works like a charm
    private fun poundConversions(x: String): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Mass.apply {
                if (topPosition in 0..16 || bottomPosition in 0..16) {
                    // g to lb or vice versa
                    constant = gramToPoundConstant
                    return somethingGramToPound(x, simplifyKgConversions())
                }
                if (topPosition == 18 || bottomPosition == 18) {
                    // pound to ounce
                    constant = poundToOunceConstant
                    return poundToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //pound to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    constant = gramToPoundConstant.scaleByPowerOfTen(-3)
                    return poundToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //pound to short ton
                    constant = shortTonToPoundConstant
                    return poundToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //pound to long ton
                    constant = poundToLonTonConstant
                    return poundToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //pound to carat
                    constant = poundToCaratConstant
                    return poundToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToPoundConstant
                    return poundToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = troyPoundToPoundConstant
                    return troyPoundToPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToPoundConstant
                    return troyOunceToPound(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToPoundConstant
                    return pennyWeightToPound(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToPoundConstant
                    return stoneToPound(x, simplifyLbConversions())

                }
            }
        }
        return null
    }

    private fun metricTonConversions(x: String): String? {
        if (topPosition == 19 || bottomPosition == 19) {
            Mass.apply {
                if (topPosition == 20 || bottomPosition == 20) {
                    //short ton to metric ton
                    constant = shortTonToMetricTonConstant
                    return shortTonToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    constant = metricTonToLonTonConstant
                    return metricTonToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //metric ton to carat
                    constant = metricTonToCaratConstant
                    return metricTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    // to grain
                    constant = grainToMetricTonConstant
                    return grainToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = metricTonTroyPoundConstant
                    return troyPoundToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToMetricTonConstant
                    return troyOunceToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToMetricTonConstant
                    return pennyWeightToMetricTon(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToMetricTonConstant
                    return stoneToMetricTon(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun ounceConversions(x: String): String? {
        if (topPosition == 18 || bottomPosition == 18) {
            Mass.apply {
                if (topPosition == 19 || bottomPosition == 19) {
                    //ounce to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    constant = gramToOunceConstant.scaleByPowerOfTen(-3)
                    return ounceToMetricTon(x, simplifyLbConversions())
                }
                if (bottomPosition == 20 || topPosition == 20) {
                    //ounce to short ton
                    constant = ounceToShortTonConstant
                    return ounceToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //ounce to long ton
                    constant = ounceToLongTonConstant
                    return ounceToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //ounce to carat
                    constant = ounceToCaratConstant
                    return ounceToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToOunceConstant
                    return ounceToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy Pound
                    constant = troyPoundToOunceConstant
                    return troyPoundToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToOunceConstant
                    return troyOunceToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToOunceConstant
                    return pennyWeightToOunce(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToOunceConstant
                    return stoneToOunce(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun shortTonConversions(x: String): String? {
        if (topPosition == 20 || bottomPosition == 20) {
            Mass.apply {
                if (topPosition == 21 || bottomPosition == 21) {
                    //short Ton to long ton
                    constant = shortTonToLongConstant
                    return shortTonToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    constant = shortTonToCaratConstant
                    return shortTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToShortTonConstant
                    return grainToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = shortTonToTroyPound
                    return troyPoundToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    constant = troyOunceToShortTonConstant
                    return troyOunceToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyweight
                    constant = pennyWeightToShortTonConstant
                    return pennyWeightToShortTon(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToShortTonConstant
                    return basicFunction(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun longTonConversions(x: String): String? {
        if (topPosition == 21 || bottomPosition == 21) {
            Mass.apply {
                if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    constant = longTonToCaratConstant
                    return longTonToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToLongTonConstant
                    return grainToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to troy pound
                    constant = longTonToTroyPoundConstant
                    return longTonToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToLongTonConstant
                    return troyOunceToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    constant = pennyWeightToLongTonConstant
                    return pennyWeightToLongTon(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    constant = stoneToLonTonConstant
                    return basicFunction(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun caratConversions(x: String): String? {
        if (topPosition == 22 || bottomPosition == 22) {
            Mass.apply {
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    constant = grainToCaratConstant
                    return grainToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = caratToTroyPoundConstant
                    return caratToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = caratToTroyOunceConstant
                    return troyOunceToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToCaratConstant
                    return pennyWeightToCarat(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone

                }
            }
        }
        return null
    }

    private fun grainConversions(x: String): String? {
        if (topPosition == 23 || bottomPosition == 23) {
            Mass.apply {
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    constant = grainToTroyPoundConstant
                    return troyPoundToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToGrainConstant
                    return troyOunceToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToGrainConstant
                    return pennyWeightToGrain(x, simplifyLbConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    //to stone
                    constant = stoneToGrainConstant
                    return basicFunction(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun troyPoundConversion(x: String): String? {
        if (topPosition == 24 || bottomPosition == 24) {
            Mass.apply {
                //troy pound
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    constant = troyOunceToTroyPoundConstant
                    return troyOunceToTroyPound(x, simplifyLbConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToTroyPoundConstant
                    return pennyWeightToTroyPound(x, simplifyLbConversions())
                }
            }
        }
        return null
    }

    private fun troyOunceConversions(x: String): String? {
        if (topPosition == 25 || bottomPosition == 25) {
            Mass.apply {
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    constant = pennyWeightToTroyOunceConstant
                    return pennyWeightToTroyOunce(x, simplifyLbConversions())
                }
            }
        }
        return null
    }
}