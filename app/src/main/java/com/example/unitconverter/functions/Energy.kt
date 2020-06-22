package com.example.unitconverter.functions

import com.example.unitconverter.constants.Energy.amongJoule
import com.example.unitconverter.constants.Energy.amongTNT
import com.example.unitconverter.constants.Energy.amongWatt
import com.example.unitconverter.constants.Energy.energy
import com.example.unitconverter.subclasses.Positions

class Energy(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPrefixes(0..17) { amongJoule } ?: jouleConversions()
        ?: amongPrefixes(18..26) { amongTNT } ?: tntConversions() ?: footPoundConversions()
        ?: electronVoltConversion() ?: amongPrefixes(30..33) { amongWatt }
        ?: thermalUnitConversion() ?: TODO()

/*
    private fun amongJoule(): String? {
        rangeAssertAnd(0..17) {
            amongJoule {
                return innerAmongPrefix(it)
            }
        }
        return null
    }*/
/*
    private fun amongTNT(): String? {
        rangeAssertAnd(18..26) {
            amongTNT {
                return innerAmongPrefix(it)
            }
        }
        return null
    }*/
/*
    private fun amongWatt(): String? {
        rangeAssertAnd(30..33) {
            amongWatt {
                return innerAmongPrefix(it)
            }
        }
        return null
        }*/

    private val joulePrefixes = {
        //amongJoule {
        innerMultiplePrefix(amongJoule)
        //}
        swapConversions()
    }

    private val tntPrefixes = {
        //amongTNT {
        innerMultiplePrefix(amongTNT)
        //}
        swapConversions()
    }

    private val wattPrefixes = {
        //amongWatt {
        innerMultiplePrefix(amongWatt)
        //}
        swapConversions()
    }

    private fun jouleConversions(): String? {
        rangeAssertOr(0..17) {
            energy {
                ratio = when {
                    rangeAssertOr(18..26) -> {
                        //to tnt and calories
                        ratio = jouleToCalorie
                        return forMultiplePrefixes(addPowers(tntPrefixes, joulePrefixes))
                    }
                    intAssertOr(27) -> {
                        //to foot pound
                        jouleToFootPound
                    }
                    intAssertOr(28) -> {
                        //to electron volt
                        electronVoltToJoule
                    }
                    intAssertOr(29) -> {
                        //to thermal unit
                        jouleToThermalUnit
                    }
                    rangeAssertOr(30..33) -> {
                        //to watt hour
                        ratio = jouleToWatt
                        return forMultiplePrefixes(addPowers(wattPrefixes, joulePrefixes))
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(joulePrefixes())
            }
        }
        return null
    }

    private fun tntConversions(): String? {
        rangeAssertOr(18..26) {
            energy {
                ratio = when {
                    intAssertOr(27) -> {
                        //to foot pound
                        footPoundToTnt
                    }
                    intAssertOr(28) -> {
                        //to electron volt
                        eVToTNT
                    }
                    intAssertOr(29) -> {
                        //to thermal unit
                        thermalUnitToTNT
                    }
                    rangeAssertOr(30..33) -> {
                        //to watt hour
                        ratio = wattToTNT
                        return forMultiplePrefixes(addPowers(wattPrefixes, tntPrefixes))
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(tntPrefixes())
            }
        }
        return null
    }

    private fun footPoundConversions(): String? {
        intAssertOr(27) {
            energy {
                ratio = when {
                    intAssertOr(28) -> {
                        //to electron volts
                        evToFootPound
                    }
                    intAssertOr(29) -> {
                        //to thermal unit
                        thermalUnitToFootPound
                    }
                    rangeAssertOr(30..33) -> {
                        //to watt hour
                        ratio = wattToFootPound
                        return wattPrefixes().let {
                            val temp = bottom
                            bottom = top
                            top = temp
                            forMultiplePrefixes(it)
                        }
                    }
                    else -> TODO()
                }
                return result
            }
        }
        return null
    }

    private fun electronVoltConversion(): String? {
        intAssertOr(28) {
            energy {
                ratio = when {
                    intAssertOr(29) -> {
                        //to thermal unit
                        thermalUnitToElectron
                    }
                    rangeAssertOr(30..33) -> {
                        //to watt hour
                        ratio = wattToeV
                        return wattPrefixes().let {
                            val temp = top
                            top = bottom
                            bottom = temp
                            forMultiplePrefixes(it)
                        }
                    }
                    else -> TODO()
                }
                return result
            }
        }
        return null
    }

    private fun thermalUnitConversion(): String? {
        intAssertOr(29) {
            rangeAssertOr(30..33) {
                energy {
                    ratio = wattToThermalUnit
                    return wattPrefixes().let {
                        val temp = bottom
                        bottom = top
                        top = temp
                        forMultiplePrefixes(it)
                    }
                }
            }
        }
        return null
    }

    private inline fun addPowers(first: () -> Int, second: () -> Int): Int {
        first()
        val tempTop = top
        val tempBottom = bottom
        val pow = second()
        top -= tempTop
        bottom -= tempBottom
        return pow
    }
}