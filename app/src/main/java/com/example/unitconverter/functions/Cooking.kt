package com.example.unitconverter.functions

import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.constants.BigDecimalsAddOns
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.Volume
import com.example.unitconverter.subclasses.Positions
import java.math.BigDecimal

class Cooking(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        conversionMap.apply {
            val topRatio = getValue(topPosition)
            val bottomRatio = getValue(bottomPosition)
            ratio = topRatio.divide(bottomRatio, BigDecimalsAddOns.mathContext)
            return basicFunction(1)
        }
    }

    private val conversionMap
        get() = buildMutableMap<Int, BigDecimal> {
            //from fl oz perspective
            put {
                //pinch
                key = 0
                value = inverseOf(BigDecimal(128))
            }
            put {
                //drop
                key = 1
                value = inverseOf(BigDecimal(576))
            }
            put {
                //coffeespoon
                key = 2
                value = inverseOf(BigDecimal(16))
            }
            put {
                ////teaspoon
                key = 3
                value = inverseOf(BigDecimal(6))
            }
            put {
                // tablespoon
                key = 4
                value = BigDecimal("0.5")
            }
            put {
                //dessertspoon
                key = 5
                value = inverseOf(BigDecimal(3))
            }
            put {
                //fl oz
                key = 6
                value = BigDecimal.ONE
            }
            put {
                //mL
                key = 7
                value = inverseOf(Volume.metreToFlOz.scaleByPowerOfTen(6))
            }
            put {
                //wineglass
                key = 8
                value = BigDecimal(2)
            }
            put {
                //cup
                key = 9
                value = BigDecimal.valueOf(8)
            }
        }
}