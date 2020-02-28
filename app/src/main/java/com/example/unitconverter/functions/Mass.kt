package com.example.unitconverter.functions

import com.example.unitconverter.constants.Mass
import com.example.unitconverter.subclasses.Positions

class Mass(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText() =
        amongGram() ?: poundConversions()
        ?: gramConversions() ?: ounceConversions()
        ?: metricTonConversions() ?: shortTonConversions()
        ?: longTonConversions() ?: caratConversions()
        ?: grainConversions() ?: troyPoundConversion()
        ?: troyOunceConversions() ?: pennyWeightConversions()
        ?: stoneConversions() ?: slugConversions()
        ?: amuConversion() ?: planckMassConversion()
        ?: throw TODO("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

    private fun amongGram(): String? {
        //val sparseArray = buildPrefixMass()
        // means its amongst the gram family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Mass.buildPrefixMass().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun gramConversions(): String? {
        // among gram
        if (topPosition in 0..16 || bottomPosition in 0..16) {
            Mass.apply {
                if (topPosition == 17 || bottomPosition == 17) {
                    // g to lb or vice versa
                    ratio = gramToPoundConstant
                    return forMultiplePrefixes(simplifyMultiplePrefix())
                }
                //gram to ounce (oz) or vice versa
                if (topPosition == 18 || bottomPosition == 18) {
                    //gram to ounce (oz) or vice versa
                    ratio = gramToOunceConstant
                } else

                    if (topPosition == 19 || bottomPosition == 19) {
                        //gram to metric ton
                        gramToMetricTonConversion()
                        return prefixMultiplication(inputString)
                    } else if (topPosition == 20 || bottomPosition == 20) {
                        //gram to short Ton
                        ratio = shortTonToKgConstant
                    } else if (topPosition == 21 || bottomPosition == 21) {
                        //gram to long Ton
                        ratio = gramToLonTonConstant
                    } else if (topPosition == 22 || bottomPosition == 22) {
                        //gram to carat
                        ratio = gramToCaratConstant
                    } else if (topPosition == 23 || bottomPosition == 23) {
                        //to grain
                        ratio = grainToGramConstant
                    } else if (topPosition == 24 || bottomPosition == 24) {
                        //to troy pound
                        ratio = gramToTroyPoundConstant
                    } else if (topPosition == 25 || bottomPosition == 25) {
                        // to troy ounce
                        ratio = troyOunceToGramConstant
                    } else if (topPosition == 26 || bottomPosition == 26) {
                        //to pennyWeight
                        ratio = pennyWeightToGramConstant
                    } else if (topPosition == 27 || bottomPosition == 27) {
                        // to stone
                        ratio = stoneToGramConstant
                    } else if (topPosition == 28 || bottomPosition == 28) {
                        // to slug
                        ratio = gramToSlugConstant
                    } else if (topPosition == 29 || bottomPosition == 29) {
                        //to amu
                        ratio = amuToKg
                    } else if (topPosition == 30 || bottomPosition == 30) {
                        // to planck mas
                        ratio = planckMassToKg
                    } else if (topPosition == 31 || bottomPosition == 31) {
                        //final solar mass
                        ratio = solarMassToKg
                    } else TODO()

                val pow = simplifyMultiplePrefix()
                return forMultiplePrefixes(pow)
            }
        }
        return null
    }

    private fun gramToMetricTonConversion() {
        Mass.apply {
            buildPrefixMass().also {
                val temp = it[topPosition, -200]
                val metricTonPosition = it[5]
                //
                val whichOne =
                    if (temp == -200) it[bottomPosition] else temp

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

    private fun simplifyMultiplePrefix(): Int {
        //to prevent double calling
        Mass.buildPrefixMass().also {
            val temp = it[topPosition, -200]
            val kgPosition = it[6]
            //which one is not kilogram??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp

            top = whichOne
            bottom = kgPosition
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    //it works like a charm
    private fun poundConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Mass.apply {
                if (topPosition == 18 || bottomPosition == 18) {
                    // pound to ounce
                    ratio = poundToOunceConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 19 || bottomPosition == 19) {
                    //pound to metric ton
                    //since the constant is for kilo it has to be divided
                    //by 1000
                    ratio = gramToPoundConstant.scaleByPowerOfTen(-3)
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 20 || bottomPosition == 20) {
                    //pound to short ton
                    ratio = shortTonToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    //pound to long ton
                    ratio = poundToLonTonConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 22 || bottomPosition == 22) {
                    //pound to carat
                    ratio = poundToCaratConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = troyPoundToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToPoundConstant
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToPound
                    return basicFunction(swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mas
                    ratio = planckMassToPound
                    return basicFunction(swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToPound
                    return basicFunction(swapConversions())
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
                    return basicFunction(swapConversions())
                }
                if (topPosition == 21 || bottomPosition == 21) {
                    ratio = metricTonToLonTonConstant
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //metric ton to carat
                    ratio = metricTonToCaratConstant
                } else if (topPosition == 23 || bottomPosition == 23) {
                    // to grain
                    ratio = grainToMetricTonConstant
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = metricTonTroyPoundConstant
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToMetricTonConstant
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToMetricTonConstant
                } else if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToMetricTonConstant
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = metricTonToSlugConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToMetricTon
                } else if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToMetricTon
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToMetricTon
                } else TODO()

                return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (bottomPosition == 20 || topPosition == 20) {
                    //ounce to short ton
                    ratio = ounceToShortTonConstant
                } else if (topPosition == 21 || bottomPosition == 21) {
                    //ounce to long ton
                    ratio = ounceToLongTonConstant
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //ounce to carat
                    ratio = ounceToCaratConstant
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToOunceConstant
                    return basicFunction(-swapConversions())
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to troy Pound
                    ratio = troyPoundToOunceConstant
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToOunceConstant
                } else if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToOunceConstant
                } else if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToOunceConstant
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToOunceConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToOunce
                } else if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToOunce
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToOunce
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 22 || bottomPosition == 22) {
                    //to carat
                    ratio = shortTonToCaratConstant
                    return basicFunction(-swapConversions())
                } else if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToShortTonConstant
                } else if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = shortTonToTroyPound
                    return basicFunction(-swapConversions())
                } else if (topPosition == 25 || bottomPosition == 25) {
                    //to troy ounce
                    ratio = troyOunceToShortTonConstant
                    return basicFunction(-swapConversions())
                } else if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyweight
                    ratio = pennyWeightToShortTonConstant
                } else if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToShortTonConstant
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToShortTonConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToShotTon
                } else if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = plankMassToShortTon
                } else if (topPosition == 31 || bottomPosition == 31) {
                    // to solar mass
                    ratio = solarMassToShortTon
                } else TODO()

                return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 23 || bottomPosition == 23) {
                    //to grain
                    ratio = grainToLongTonConstant
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    // to troy pound
                    ratio = longTonToTroyPoundConstant
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToLongTonConstant

                    return basicFunction(-swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    //to pennyWeight
                    ratio = pennyWeightToLongTonConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToLonTonConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToLongTonConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToLongTon
                    return basicFunction(swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToLongTon
                    return basicFunction(swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    // to solar mass
                    ratio = solarMassToLongTon
                    return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 24 || bottomPosition == 24) {
                    //to troy pound
                    ratio = caratToTroyPoundConstant
                } else if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = caratToTroyOunceConstant
                } else if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToCaratConstant
                } else if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToCaratConstant
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToCaratConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToCarat
                } else if (topPosition == 30 || bottomPosition == 30) {
                    // to planck mass
                    ratio = planckMassToCarat
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToCarat
                } else TODO()

                return basicFunction(swapConversions())
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
                } else if (topPosition == 25 || bottomPosition == 25) {
                    // to troy ounce
                    ratio = troyOunceToGrainConstant
                } else if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToGrainConstant
                } else if (topPosition == 27 || bottomPosition == 27) {
                    //to stone
                    ratio = stoneToGrainConstant
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToGrainConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToGrain
                } else if (topPosition == 30 || bottomPosition == 30) {
                    ratio = planckMassToGrain
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToGrain
                } else TODO()

                return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 26 || bottomPosition == 26) {
                    // to pennyWeight
                    ratio = pennyWeightToTroyPoundConstant
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToTroyPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToTroyPoundConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToTroyPound
                    return basicFunction(swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToTroyPound
                    return basicFunction(swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToTroyPound
                    return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 27 || bottomPosition == 27) {
                    // to stone
                    ratio = stoneToTroyOunceConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToTroyOunceConstant
                    return basicFunction(swapConversions())
                }
                if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    ratio = amuToTroyOunce
                    return basicFunction(swapConversions())
                }
                if (topPosition == 30 || bottomPosition == 30) {
                    // to planck
                    ratio = planckMassToTroyOunce
                    return basicFunction(swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToTroyOunce
                    return basicFunction(swapConversions())
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
                } else if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    ratio = slugToPennyWeight
                } else if (topPosition == 29 || bottomPosition == 29) {
                    ratio = amuToPennyWeight
                    //to amu
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    ratio = planckMassToPennyWeight
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToPennyWeight
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun stoneConversions(): String? {
        if (topPosition == 27 || bottomPosition == 27) {
            Mass.apply {
                ratio = if (topPosition == 28 || bottomPosition == 28) {
                    // to slug
                    slugToStoneConstant
                } else if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    amuToStone
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    planckMassToStone
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    solarMassToStone
                } else TODO()

                return basicFunction(swapConversions())
            }
        }
        return null
    }

    private fun slugConversions(): String? {
        if (topPosition == 28 || bottomPosition == 28) {
            Mass.apply {
                ratio = if (topPosition == 29 || bottomPosition == 29) {
                    //to amu
                    amuToSlug
                } else if (topPosition == 30 || bottomPosition == 30) {
                    //to planck mass
                    planckMassToSlug
                } else if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    solarMassToSlug
                } else TODO()

                return basicFunction(swapConversions())
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
                    return basicFunction(-swapConversions())
                }
                if (topPosition == 31 || bottomPosition == 31) {
                    //to solar mass
                    ratio = solarMassToAmu
                    return basicFunction(swapConversions())
                }
            }
        }
        return null
    }

    // last function  yaaaaaaaaaaaay
    private fun planckMassConversion(): String? {
        if (topPosition == 30 || bottomPosition == 30) {
            if (topPosition == 31 || bottomPosition == 31) {
                ratio = Mass.solarMassToPlanckMass
                return basicFunction(swapConversions())
            }
        }
        return null
    }
}