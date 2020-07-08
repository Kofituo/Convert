package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Angle
import com.otuolabs.unitconverter.subclasses.Positions

class Angle(override val positions: Positions) : ConstantsAbstractClass() {

    private inline val pow get() = swapConversions()

    override fun getText(): String =
        degreeConversions() ?: radiansConversions() ?: gradiansConversions() ?: revConversions()
        ?: minuteConversions() ?: secondConversions() ?: quadrantConversions()
        ?: sextantConversion()
        ?: throw TODO("top position = $topPosition  bottom position = $bottomPosition")//just in case i forgot one

    private fun degreeConversions(): String? {
        if (topPosition == 0 || bottomPosition == 0) {
            Angle.apply {
                if (topPosition == 1 || bottomPosition == 1) {
                    //to radians
                    ratio = degreesToRadians
                } else if (topPosition == 2 || bottomPosition == 2) {
                    //to gradians
                    ratio = degreesToGradians
                } else if (topPosition == 3 || bottomPosition == 3) {
                    //to revolution
                    ratio = degreesToRevolution
                } else if (topPosition == 4 || bottomPosition == 4) {
                    //to minute
                    ratio = degreesToMinute
                } else if (topPosition == 5 || bottomPosition == 5) {
                    //to seconds
                    ratio = degreeToSeconds
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    ratio = degreesToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    ratio = degreesToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    ratio = degreesToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun radiansConversions(): String? {
        if (topPosition == 1 || bottomPosition == 1) {
            Angle.apply {
                if (topPosition == 2 || bottomPosition == 2) {
                    // to gradians
                    ratio = radiansToGradians
                } else if (topPosition == 3 || bottomPosition == 3) {
                    //to turn
                    ratio = radiansToRevolutions
                } else if (topPosition == 4 || bottomPosition == 4) {
                    //to minute
                    ratio = radiansToMinute
                } else if (topPosition == 5 || bottomPosition == 5) {
                    //to seconds
                    ratio = radiansToSeconds
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    ratio = radiansToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    ratio = radiansToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    ratio = radiansToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun gradiansConversions(): String? {
        if (topPosition == 2 || bottomPosition == 2) {
            Angle.apply {
                if (topPosition == 3 || bottomPosition == 3) {
                    //to revolutions
                    ratio = gradiansToRevolutions
                } else if (topPosition == 4 || bottomPosition == 4) {
                    //to minute
                    ratio = gradiansToMinute
                } else if (topPosition == 5 || bottomPosition == 5) {
                    //to seconds
                    ratio = gradiansToSeconds
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    ratio = gradiansToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    ratio = gradiansToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    ratio = gradiansToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun revConversions(): String? {
        if (topPosition == 3 || bottomPosition == 3) {
            Angle.apply {
                if (topPosition == 4 || bottomPosition == 4) {
                    //to minute
                    ratio = revToMinute
                } else if (topPosition == 5 || bottomPosition == 5) {
                    //to seconds
                    ratio = revToSeconds
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    ratio = revToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    ratio = revolutionToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    ratio = revToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun minuteConversions(): String? {
        if (topPosition == 4 || bottomPosition == 4) {
            Angle.apply {
                ratio = if (topPosition == 5 || bottomPosition == 5) {
                    //to seconds
                    minuteToSeconds
                } else if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    minuteToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    minuteToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    minuteToOctant
                } else TODO()
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun secondConversions(): String? {
        if (topPosition == 5 || bottomPosition == 5) {
            Angle.apply {
                ratio = if (topPosition == 6 || bottomPosition == 6) {
                    //to quadrant
                    secondsToQuadrant
                } else if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    secondToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    secondToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun quadrantConversions(): String? {
        if (topPosition == 6 || bottomPosition == 6) {
            Angle.apply {
                ratio = if (topPosition == 7 || bottomPosition == 7) {
                    //to sextant
                    quadrantsToSextant
                } else if (topPosition == 8 || bottomPosition == 8) {
                    //to octant
                    quadrantToOctant
                } else TODO()

                return basicFunction(pow)
            }
        }
        return null
    }

    private fun sextantConversion(): String? {
        if (topPosition == 7 || bottomPosition == 7) {
            if (topPosition == 8 || bottomPosition == 8) {
                //to octant
                ratio = Angle.sextantToOctant
                return basicFunction(pow)
            }
        }
        return null
    }
}