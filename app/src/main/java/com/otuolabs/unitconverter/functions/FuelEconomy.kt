package com.otuolabs.unitconverter.functions

import android.util.SparseIntArray
import com.otuolabs.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.otuolabs.unitconverter.constants.FuelEconomy
import com.otuolabs.unitconverter.subclasses.Positions
import java.math.BigDecimal

class FuelEconomy(override val positions: Positions) : ConstantsAbstractClass() {
    override fun getText(): String =
        amongMetreLitre() ?: metrePerLitreConversions() ?: milePerLitreConversions()
        ?: footPerLitreConversions() ?: inchPerLitreConversions() ?: miPerUsGallonConversions()
        ?: miPerImpGalConversion() ?: amongMetreUsGallon() ?: metrePerGallonConversions()
        ?: amongMetreUkGallon() ?: metrePerUkGalConversions()
        ?: TODO()

    private fun amongMetreLitre(): String? {
        if (topPosition in 0..4 && bottomPosition in 0..4)
            sparseIntArray(0) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun amongMetreUsGallon(): String? {
        if (topPosition in 10..14 && bottomPosition in 10..14)
            sparseIntArray(10) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun amongMetreUkGallon(): String? {
        if (topPosition in 15..19 && bottomPosition in 15..19)
            sparseIntArray(15) {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        return null
    }

    private fun multipleMetreLitrePrefix(): Int {
        //to prevent double calling
        sparseIntArray(0) {
            val temp = it[topPosition, -200]
            //which one is not metre Litre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (topPosition > bottomPosition) 1 else -1
    }

    private fun multipleMetreGalPrefix(): Int {
        //to prevent double calling
        sparseIntArray(10) {
            val temp = it[topPosition, -200]
            //which one is not metre Litre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (topPosition < bottomPosition) 1 else -1
    }

    private fun multipleMetreUkGalPrefix(): Int {
        //to prevent double calling
        sparseIntArray(15) {
            val temp = it[topPosition, -200]
            //which one is not metre Litre??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
        }
        return if (topPosition < bottomPosition) 1 else -1
    }

    private fun metrePerLitreConversions(): String? {
        if (topPosition in 0..4 || bottomPosition in 0..4)
            fuel {
                ratio = when {
                    topPosition == 5 || bottomPosition == 5 -> {
                        //to mile per litre
                        metreToMileLitre
                    }
                    topPosition == 6 || bottomPosition == 6 -> {
                        //to foot per litre
                        metreToFootLitre
                    }
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        metreToInchLitre
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to miles per us gallon
                        miPerUsGalToMPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to miles per uk gallon
                        miPerUkGalToMPL
                    }
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        multipleMetreLitrePrefix()
                        val tempTop = top
                        val tempBottom = bottom
                        val pow = multipleMetreGalPrefix()
                        top -= tempTop
                        bottom -= tempBottom
                        ratio = litreToUSGalMetre
                        return forMultiplePrefixes(pow)
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        multipleMetreLitrePrefix()
                        val tempTop = top
                        val tempBottom = bottom
                        val pow = multipleMetreUkGalPrefix()
                        top -= tempTop
                        bottom -= tempBottom
                        ratio = litreToUkGalMetre
                        return forMultiplePrefixes(pow)
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litre per 100 km
                        ratio = mePerLitToLiPer100km
                        multipleMetreLitrePrefix()
                        return metrePerLitreToLitrePer100km()
                    }
                    else -> TODO()
                }
                return forMultiplePrefixes(multipleMetreLitrePrefix())
            }
        return null
    }

    private fun milePerLitreConversions(): String? {
        if (topPosition == 5 || bottomPosition == 5)
            fuel {
                ratio = when {
                    topPosition == 6 || bottomPosition == 6 -> {
                        //to foot per litre
                        mileToFootLitre
                    }
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        mileToInchLiter
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per gallon
                        litreToGallonMile
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        litreToUkGallonMile
                    }
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        ratio = mipLToMetrePerGallon
                        return forMultiplePrefixes(multipleMetreGalPrefix())
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        ratio = mipLToMetrePerUkGallon
                        return forMultiplePrefixes(multipleMetreUkGalPrefix())
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litre per 100 km
                        ratio = milePerLitreToLitrePer100km
                        return toLitrePer100km()
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun footPerLitreConversions(): String? {
        if (topPosition == 6 || bottomPosition == 6)
            fuel {
                ratio = when {
                    topPosition == 7 || bottomPosition == 7 -> {
                        //to inch per litre
                        footToInchLitre
                    }
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per gallon
                        miPerGalToFPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        miPerUkGalToFPL
                    }
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        ratio = feetPerLitreToMPGal
                        return forMultiplePrefixes(multipleMetreGalPrefix())
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        ratio = feetPerLitreToMPUkGal
                        return forMultiplePrefixes(multipleMetreUkGalPrefix())
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litres per 100km
                        ratio = feetPerLitreToLitrePer100km
                        return toLitrePer100km()
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun inchPerLitreConversions(): String? {
        if (topPosition == 7 || bottomPosition == 7)
            fuel {
                ratio = when {
                    topPosition == 8 || bottomPosition == 8 -> {
                        //to mile per us gallon
                        miPerGalToIPL
                    }
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per uk gallon
                        miPerUKGalToIPL
                    }
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        ratio = inchPerLitreToMPGal
                        return forMultiplePrefixes(multipleMetreGalPrefix())
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        ratio = inchPerLitreToMPUkGal
                        return forMultiplePrefixes(multipleMetreUkGalPrefix())
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litres per 100km
                        ratio = inchPerLitreToLitrePer100km
                        return toLitrePer100km()
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun miPerUsGallonConversions(): String? {
        if (topPosition == 8 || bottomPosition == 8)
            fuel {
                ratio = when {
                    topPosition == 9 || bottomPosition == 9 -> {
                        //to mile per imp gallon
                        usGalToUkGalMile
                    }
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        ratio = miPerGalToMePerUsGal
                        return forMultiplePrefixes(multipleMetreGalPrefix())
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        ratio = miPerGalToMePerUkGal
                        return forMultiplePrefixes(multipleMetreUkGalPrefix())
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litres per 100km
                        ratio = mpgUsLitreToLitrePer100km
                        return toLitrePer100km()
                    }
                    else -> TODO()
                }
                return result
            }
        return null
    }

    private fun miPerImpGalConversion(): String? {
        if (topPosition == 9 || bottomPosition == 9)
            fuel {
                return when {
                    topPosition in 10..14 || bottomPosition in 10..14 -> {
                        //to metre per us gallon
                        ratio = miPerUkGalToMePerUsGal
                        forMultiplePrefixes(multipleMetreGalPrefix())
                    }
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        //to metre per uk gallon
                        ratio = miPerUkGalToMePerUkGal
                        forMultiplePrefixes(multipleMetreUkGalPrefix())
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        //to litres per 100km
                        ratio = mpgUkLitreToLitrePer100km
                        toLitrePer100km()
                    }
                    else -> TODO()
                }
            }
        return null
    }

    private fun metrePerGallonConversions(): String? {
        if (topPosition in 10..14 || bottomPosition in 10..14)
            fuel {
                ratio = when {
                    topPosition in 15..19 || bottomPosition in 15..19 -> {
                        /**to metre per uk gallon**/
                        multipleMetreGalPrefix()
                        val tempTop = top
                        val tempBottom = bottom
                        val pow = multipleMetreUkGalPrefix()
                        top -= tempTop
                        bottom -= tempBottom
                        ratio = mePerUSGalToMePerUkGallon
                        return forMultiplePrefixes(pow)
                    }
                    topPosition == 20 || bottomPosition == 20 -> {
                        /** to litre per 100 km*/
                        multipleMetreGalPrefix()
                        mePerGalToLitrePer100km
                    }
                    else -> TODO()
                }
                return metrePerLitreToLitrePer100km()
            }
        return null
    }

    private fun metrePerUkGalConversions(): String? {
        if (topPosition in 15..19 || bottomPosition in 15..19)
            fuel {
                if (topPosition == 20 || bottomPosition == 20) {
                    /** to litre per 100 km*/
                    multipleMetreUkGalPrefix()
                    ratio = mePerUkGalToLitrePer100km
                    return metrePerLitreToLitrePer100km()
                }
            }
        return null
    }

    private inline fun fuel(block: FuelEconomy.() -> Unit) = FuelEconomy.apply(block)

    private inline fun sparseIntArray(start: Int, block: (SparseIntArray) -> Unit) =
        FuelEconomy.sparseIntArray(start).also(block)

    private fun metrePerLitreToLitrePer100km(): String {
        val scale = prefix()
        val divisor = BigDecimal(inputString)
        if (divisor.compareTo(BigDecimal.ZERO) == 0) return inputString
        return ratio
            .scaleByPowerOfTen(if (scale < -1) scale else -scale) //to cater for deca metre and deci metre
            .divide(divisor, mathContext)
            .toStringWithCommas()
    }

    private fun toLitrePer100km(): String {
        val divisor = BigDecimal(inputString)
        if (divisor.compareTo(BigDecimal.ZERO) == 0) return inputString
        return ratio
            .divide(divisor, mathContext)
            .toStringWithCommas()
    }
}