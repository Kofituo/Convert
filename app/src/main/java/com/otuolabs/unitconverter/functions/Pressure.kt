package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.constants.Pressure
import com.otuolabs.unitconverter.subclasses.Positions

class Pressure(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String =
        amongPascal() ?: amongMercury() ?: pascalToBar() ?: amongBar() ?: pascalAndBarConversions()
        ?: inchOfMercuryConversions() ?: mercuryConversions() ?: gramForceConversions()
        ?: kgForceConversions() ?: psiConversions() ?: atmConversion()
        ?: TODO()

    private fun amongPascal(): String? {
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            Pressure.pascalConversions().also {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun amongMercury(): String? {
        if (topPosition in 18..20 && bottomPosition in 18..20) {
            Pressure.mercuryConversions().apply {
                top = get(topPosition)
                bottom = get(bottomPosition)
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun simplifyMultiplePrefixMercury(): Int {
        Pressure.mercuryConversions().apply {
            val temp = get(topPosition, 10)
            //which one is mercury
            val whichOne =
                if (temp == 10) get(bottomPosition) else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun amongBar(): String? {
        if (topPosition in 21..29 && bottomPosition in 21..29) {
            Pressure.barToPascalConversions().apply {
                top = get(topPosition)
                bottom = get(bottomPosition)
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    private fun simplifyMultiplePrefix(): Int {
        Pressure.pascalConversions().also {
            val temp = it[topPosition, -200]
            //which one is not pascal??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun simplifyMultiplePrefixBar(): Int {
        Pressure.barToPascalConversions().also {
            val temp = it[topPosition, -200]
            //which one is not bar??
            val whichOne =
                if (temp == -200) it[bottomPosition] else temp
            top = whichOne
            bottom = 0
            return if (topPosition > bottomPosition) 1 else -1
        }
    }

    private fun pascalToBar(): String? {
        if ((topPosition in 0..16 || bottomPosition in 0..16) &&
            (topPosition in 21..29 || bottomPosition in 21..29)
        ) {
            with(Pressure) {
                pascalConversions().apply {
                    barToPascalConversions().also {
                        val somethingBar =
                            if (it[topPosition, 200] == 200) it[bottomPosition] else it[topPosition]

                        val somethingPascal =
                            if (get(topPosition, 100) == 100) get(bottomPosition)
                            else get(topPosition)

                        if (swapConversions() == 1) {
                            top = somethingBar
                            bottom = somethingPascal
                        } else {
                            top = somethingPascal
                            bottom = somethingBar
                        }
                        return prefixMultiplication(inputString)
                    }
                }
            }
        }
        return null
    }

    private fun pascalAndBarConversions(): String? {
        if (topPosition in 0..16 || bottomPosition in 0..16 ||
            topPosition in 21..29 || bottomPosition in 21..29
        ) {
            Pressure.apply {
                when {
                    topPosition == 17 || bottomPosition == 17 -> {
                        //to inch of mercury
                        ratio = pascalToInchOfHg
                    }
                    topPosition in 18..20 || bottomPosition in 18..20 -> {
                        //to mercury
                        ratio = mmOfHgToPascal
                        val mercuryPow =
                            simplifyMultiplePrefixMercury() //get conversions among mercury
                        val tempTop = top
                        val tempBottom = bottom

                        // gets the powers to add
                        if (topPosition in 0..16 || bottomPosition in 0..16)
                            simplifyMultiplePrefix() //for pascals
                        else simplifyMultiplePrefixBar() //for bar
                        // multiply by the corresponding powers to convert it to pascals or bar
                        top += tempTop
                        bottom += tempBottom

                        val pow =
                            if (topPosition < 21 && bottomPosition < 21) mercuryPow else -mercuryPow
                        return forMultiplePrefixes(pow)
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to gram force per centimetre square
                        ratio = gramForceToPascal
                    }
                    topPosition == 31 || bottomPosition == 31 -> {
                        //to kilogram force per square centimetre
                        ratio = kgForceToPascal
                    }
                    topPosition == 32 || bottomPosition == 32 -> {
                        //to psi
                        ratio = pascalToPsi
                    }
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atmospheres
                        ratio = pascalToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        ratio = pascalToTorr
                    }
                    else -> TODO()
                }

                val pow =
                    if (topPosition in 0..16 || bottomPosition in 0..16) simplifyMultiplePrefix()
                    else simplifyMultiplePrefixBar()

                return forMultiplePrefixes(pow)
            }
        }
        return null
    }

    private inline val pow get() = swapConversions()

    private fun inchOfMercuryConversions(): String? {
        if (topPosition == 17 || bottomPosition == 17) {
            Pressure.apply {
                when {
                    topPosition in 18..20 || bottomPosition in 18..20 -> {
                        //to other mercury
                        // eg mm of mercury
                        ratio = mmOfHgToInchOfHg
                        val pow = simplifyMultiplePrefixMercury()
                        return forMultiplePrefixes(pow)
                    }
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to gram force per centimetre square
                        ratio = inchOfHgToGramForce
                    }
                    topPosition == 31 || bottomPosition == 31 -> {
                        //to gram force per centimetre square
                        ratio = inchOfHgToKgForce
                    }
                    topPosition == 32 || bottomPosition == 32 -> {
                        //to psi
                        ratio = inchOfHgToPsi
                    }
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atmosphere
                        ratio = inchOfHgToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        ratio = inchOfHgToTorr
                    }
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun mercuryConversions(): String? {
        if (topPosition in 18..20 || bottomPosition in 18..20) {
            Pressure.apply {
                when {
                    topPosition == 30 || bottomPosition == 30 -> {
                        //to gram force per cm sq
                        ratio = mercuryToGramForce
                    }
                    topPosition == 31 || bottomPosition == 31 -> {
                        //to kg force per cm sq
                        ratio = mercuryToKgForce
                    }
                    topPosition == 32 || bottomPosition == 32 -> {
                        //to psi
                        ratio = mercuryToPsi
                    }
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atmosphere
                        ratio = mercuryToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        ratio = mercuryToTorr
                    }
                    else -> TODO()
                }
                val pow = simplifyMultiplePrefixMercury()
                bottom = top
                top = 0
                return forMultiplePrefixes(pow)
            }
        }
        return null
    }

    private fun gramForceConversions(): String? {
        if (topPosition == 30 || bottomPosition == 30) {
            Pressure.apply {
                ratio = when {
                    topPosition == 31 || bottomPosition == 31 -> {
                        //to kilogram force
                        gramForceToKgForce
                    }
                    topPosition == 32 || bottomPosition == 32 -> {
                        //to psi
                        gramForceToPsi
                    }
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atmosphere
                        gramForceToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        gramForceToTorr
                    }
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun kgForceConversions(): String? {
        if (topPosition == 31 || bottomPosition == 31) {
            Pressure.apply {
                ratio = when {
                    topPosition == 32 || bottomPosition == 32 -> {
                        //to psi
                        kgForceToPsi
                    }
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atmosphere
                        kgForceToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        kgForceToTorr
                    }
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun psiConversions(): String? {
        if (topPosition == 32 || bottomPosition == 32) {
            Pressure.apply {
                ratio = when {
                    topPosition == 33 || bottomPosition == 33 -> {
                        //to atm
                        psiToAtm
                    }
                    topPosition == 34 || bottomPosition == 34 -> {
                        //to torr
                        psiToTorr
                    }
                    else -> TODO()
                }
                return basicFunction(pow)
            }
        }
        return null
    }

    private fun atmConversion(): String? {
        if (topPosition == 33 || bottomPosition == 33) {
            if (topPosition == 34 || bottomPosition == 34) {
                ratio = Pressure.torrToAtm
                return basicFunction(pow)
            }
        }
        return null
    }
}