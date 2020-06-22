package com.example.unitconverter.constants

import androidx.core.util.forEach
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.buildSparseIntArray
import com.example.unitconverter.builders.put
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.Mass.gramToOunceConstant
import com.example.unitconverter.constants.Mass.gramToPoundConstant
import com.example.unitconverter.constants.Mass.gramToSlugConstant
import java.math.BigDecimal

object Density {

    val amongGramPerMetre
        get() = buildSparseIntArray(15) {
            append(0, 0)
            append(1, 3) // mm
            append(2, -3) // decimetre
            append(3, -9) //deca
            append(4, -15) //kilo
            append(5, 0) // gram per ml
            append(6, 0) // kg / litre
            append(7, -3) // kg / m
            append(8, 15) // kg / micro metre
            append(9, 6) // kg / mm
            append(10, 3) // kg / cm
            append(11, 0) // kg / dm
            append(12, -6) // kg /dam
            append(13, -12)
            append(14, 0) // ton / m
        }

    val densityMap by lazy(LazyThreadSafetyMode.NONE) {
        buildMutableMap<Int, BigDecimal>(30) {
            amongGramPerMetre.forEach { key, value ->
                put {
                    this.key = key
                    this.value = BigDecimal.TEN.pow(value, mathContext)
                }
            }
            put {
                //to ounce per cubic
                key = 15
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToInch.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to ounce per fluid ounce
                key = 16
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToFlOz.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to ounce per imp fl oz
                key = 17
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToImpFlOz.scaleByPowerOfTen(3), mathContext)
            }
            put {
                // to ounce per foot
                key = 18
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToFoot.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to ounce per yard
                key = 19
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToYard.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to ounce per gal
                key = 20
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToGallon.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to ounce per imp gal
                key = 21
                value =
                    gramToOunceConstant
                        .divide(Volume.metreToImpGallon.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to pound per cubic
                key = 22
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToInch.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to Pound per fluid ounce
                key = 23
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToFlOz.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to Pound per imp fl oz
                key = 24
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToImpFlOz.scaleByPowerOfTen(3), mathContext)
            }
            put {
                // to Pound per foot
                key = 25
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToFoot.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to Pound per yard
                key = 26
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToYard.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to Pound per gal
                key = 27
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToGallon.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to Pound per imp gal
                key = 28
                value =
                    gramToPoundConstant
                        .divide(Volume.metreToImpGallon.scaleByPowerOfTen(3), mathContext)
            }
            put {
                //to slug per foot
                key = 29
                value =
                    gramToSlugConstant
                        .divide(Volume.metreToFoot.scaleByPowerOfTen(3), mathContext)
            }
        }
    }
}