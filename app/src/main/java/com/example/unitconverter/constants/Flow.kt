package com.example.unitconverter.constants

import com.example.unitconverter.builders.buildArrayMap
import com.example.unitconverter.builders.put
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import java.math.BigDecimal

object Flow {
    val flowMap =
        buildArrayMap<Int, BigDecimal>(27) {
            val sixty = BigDecimal(60)
            val threeSixty = BigDecimal(3600)
            put {
                key = 0
                value = BigDecimal.ONE
            }
            put {
                //metre per minute
                key = 1
                value = sixty
            }
            put {
                //metre per hours
                key = 2
                value = threeSixty
            }
            put {
                //centimetre per second
                key = 3
                value = BigDecimal.ONE.scaleByPowerOfTen(6)
            }
            put {
                //centimetre per minute
                key = 4
                value = sixty.scaleByPowerOfTen(6)
            }
            put {
                //centimetre per hour
                key = 5
                value = threeSixty.scaleByPowerOfTen(6)
            }
            put {
                //millimetre per second
                key = 6
                value = BigDecimal.ONE.scaleByPowerOfTen(9)
            }
            put {
                // millimetre per minute
                key = 7
                value = sixty.scaleByPowerOfTen(9)
            }
            put {
                //millimetres per hour
                key = 8
                value = threeSixty.scaleByPowerOfTen(9)
            }
            put {
                //litres per second
                key = 9
                value = BigDecimal.ONE.scaleByPowerOfTen(3)
            }
            put {
                //litres per minute
                key = 10
                value = sixty.scaleByPowerOfTen(3)
            }
            put {
                //to litres per hours
                key = 11
                value = threeSixty.scaleByPowerOfTen(3)
            }
            put {
                //millilitres per second
                key = 12
                value = BigDecimal.ONE.scaleByPowerOfTen(6)
            }
            put {
                //ml per min
                key = 13
                value = sixty.scaleByPowerOfTen(6)
            }
            put {
                //ml per hour
                key = 14
                value = threeSixty.scaleByPowerOfTen(6)
            }
            put {
                //cubic foot per second
                key = 15
                value = inverseOf(Volume.metreToFoot)
            }
            put {
                //cubic foot per minute
                key = 16
                value = sixty.divide(Volume.metreToFoot, mathContext)
            }
            put {
                //cubic foot per hour
                key = 17
                value = threeSixty.divide(Volume.metreToFoot, mathContext)
            }
            put {
                //cubic inch per second
                key = 18
                value = inverseOf(Volume.metreToInch)
            }
            put {
                //cubic inch per minute
                key = 19
                value = sixty.divide(Volume.metreToInch, mathContext)
            }
            put {
                //cubic inch per hour
                key = 20
                value = threeSixty.divide(Volume.metreToInch, mathContext)
            }
            put {
                //gallon per second
                key = 21
                value = inverseOf(Volume.metreToGallon)
            }
            put {
                //to gallon per minute
                key = 22
                value = sixty.divide(Volume.metreToGallon, mathContext)
            }
            put {
                key = 23
                value = threeSixty.divide(Volume.metreToGallon, mathContext)
            }
            put {
                //gallon per second
                key = 24
                value = inverseOf(Volume.metreToImpGallon)
            }
            put {
                //to gallon per minute
                key = 25
                value = sixty.divide(Volume.metreToImpGallon, mathContext)
            }
            put {
                key = 26
                value = threeSixty.divide(Volume.metreToImpGallon, mathContext)
            }
        }
}