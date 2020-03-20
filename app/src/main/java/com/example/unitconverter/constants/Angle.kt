package com.example.unitconverter.constants

import com.example.unitconverter.constants.BigDecimalsAddOns.divide
import com.example.unitconverter.constants.BigDecimalsAddOns.inverseOf
import com.example.unitconverter.constants.BigDecimalsAddOns.mathContext
import com.example.unitconverter.constants.BigDecimalsAddOns.multiply
import java.math.BigDecimal

object Angle {

    private inline val PI: BigDecimal get() = BigDecimal("3.14159265358979323846264338327950288")

    val degreesToRadians: BigDecimal get() = BigDecimal(180).divide(PI, mathContext)

    val degreesToGradians: BigDecimal get() = BigDecimal("0.9")

    val radiansToGradians get() = PI.divide(200, mathContext)

    val degreesToRevolution: BigDecimal get() = BigDecimal(360)

    val radiansToRevolutions get() = PI.multiply(2)

    val gradiansToRevolutions: BigDecimal get() = BigDecimal(400)

    val degreesToMinute: BigDecimal get() = inverseOf(BigDecimal(60))

    val radiansToMinute get() = PI.divide(10800, mathContext)

    val gradiansToMinute: BigDecimal get() = inverseOf(BigDecimal(54))

    val revToMinute get() = inverseOf(BigDecimal(21600))

    val degreeToSeconds get() = inverseOf(BigDecimal(3600))

    val radiansToSeconds get() = radiansToMinute.divide(60, mathContext)

    val gradiansToSeconds get() = gradiansToMinute.divide(60, mathContext)

    val revToSeconds get() = revToMinute.divide(60, mathContext)

    val minuteToSeconds get() = inverseOf(BigDecimal(60))

    val degreesToQuadrant: BigDecimal get() = BigDecimal(90)

    val radiansToQuadrant get() = PI.divide(2, mathContext)

    val gradiansToQuadrant: BigDecimal get() = BigDecimal(100)

    val revToQuadrant: BigDecimal get() = BigDecimal("0.25")

    val minuteToQuadrant: BigDecimal get() = BigDecimal(5400)

    val secondsToQuadrant: BigDecimal get() = BigDecimal(5400 * 60)

    val degreesToSextant: BigDecimal get() = BigDecimal(60)

    val radiansToSextant get() = PI.divide(3, mathContext)

    val gradiansToSextant get() = BigDecimal(100).multiply(2).divide(3, mathContext)

    val revolutionToSextant get() = inverseOf(BigDecimal(6))

    val minuteToSextant: BigDecimal get() = BigDecimal(3600)

    val secondToSextant: BigDecimal get() = BigDecimal(3600 * 60)

    val quadrantsToSextant get() = BigDecimal(6).divide(9, mathContext)

    val degreesToOctant: BigDecimal get() = BigDecimal(45)

    val radiansToOctant get() = PI.divide(4, mathContext)

    val gradiansToOctant: BigDecimal get() = BigDecimal(50)

    val revToOctant: BigDecimal get() = BigDecimal("0.125")

    val minuteToOctant: BigDecimal get() = BigDecimal(2700)

    val secondToOctant: BigDecimal get() = BigDecimal(2700 * 60)

    val quadrantToOctant: BigDecimal get() = BigDecimal("0.5")

    val sextantToOctant: BigDecimal get() = BigDecimal("0.75")
}