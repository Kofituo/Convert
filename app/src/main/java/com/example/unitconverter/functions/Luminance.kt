package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.Luminance
import com.example.unitconverter.miscellaneous.colors
import com.example.unitconverter.subclasses.Positions

class Luminance(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongCanPerMetre() ?: candelaPerMetreConversions() ?: apostilbConversion()
        ?: lambertConversions() ?: footLambertConversions() ?: footConversion()
        ?: ""

    lateinit var lazyMap: Lazy<Map<Int, Int>>

    private inline val map: Map<Int, Int> get() = lazyMap.value

    private inline val pow: Int
        //to prevent double calling
        get() = map.let {
            if (it.getValue(topPosition) > it.getValue(bottomPosition)) 1 else -1
        }

    private fun amongCanPerMetre(): String? {
        if (
            rangeAssertAnd(0..7) //among candela per metre with metric prefix
            ||
            rangeAssertAnd(13..19) // among candela per sq km and family
            ||
            rangeAssertOr(0..7) && rangeAssertOr(13..19) // from say candela per sq metre to candela per sq kilometre
        ) canPerMetre { return innerAmongPrefix(it) }
        colors
        return null
    }

    private fun candelaPrefix(): Int {
        canPerMetre {
            innerMultiplePrefix(it)
        }
        return pow
    }

    private fun candelaPerMetreConversions(): String? {
        if (rangeAssertOr(0..7) || rangeAssertOr(13..19))
            luminance {
                ratio = when {
                    intAssertOr(8) -> {
                        //to apostilb
                        apoStilbToCandela
                    }
                    intAssertOr(9) -> {
                        //to lambert
                        lambertToCandela
                    }
                    intAssertOr(10) -> {
                        //to foot lambert
                        candelaToFootLambert
                    }
                    intAssertOr(11) -> {
                        //to foot
                        candelaToFoot
                    }
                    intAssertOr(12) -> {
                        //to inch
                        candelaPerInchToCanPerMetre
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(candelaPrefix())
            }
        return null
    }

    private fun apostilbConversion(): String? {
        if (intAssertOr(8))
            luminance {
                ratio = when {
                    intAssertOr(9) -> {
                        //to lambert
                        lambertToApostilb
                    }
                    intAssertOr(10) -> {
                        //to foot lambert
                        apostilbToFootLambert
                    }
                    intAssertOr(11) -> {
                        //to candela per sq foot
                        apostilbToCandelaPerFoot
                    }
                    intAssertOr(12) -> {
                        //to candela per inch
                        apostilbToCandelaPerInch
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun lambertConversions(): String? {
        if (intAssertOr(9))
            luminance {
                ratio = when {
                    intAssertOr(10) -> {
                        //to foot lambert
                        footLambertToLambert
                    }
                    intAssertOr(11) -> {
                        //to candela per foot
                        lambertToCandelaPerFoot
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun footLambertConversions(): String? {
        if (intAssertOr(10))
            luminance {
                ratio = when {
                    intAssertOr(11) -> {
                        //to sq foot
                        footLambertToCandelaFoot
                    }
                    intAssertOr(12) -> {
                        //to sq inch
                        footLambertToCandelaInch
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun footConversion(): String? {
        if (intAssertOr(11))
            luminance {
                if (intAssertOr(12)) {
                    ratio = feetToInch
                    return result
                }
            }
        return null
    }

    private inline fun canPerMetre(block: (SparseIntArray) -> Unit) =
        Luminance.candelaPerSquareMetre().also(block)

    private inline fun luminance(block: Luminance.() -> Unit) =
        Luminance.apply(block)
}