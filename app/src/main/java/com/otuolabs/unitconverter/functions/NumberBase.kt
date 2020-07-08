package com.otuolabs.unitconverter.functions

import com.otuolabs.unitconverter.subclasses.Positions
import java.util.*

class NumberBase(override val positions: Positions) : ConstantsAbstractClass() {

    override fun getText(): String {
        inputString.toIntOrNull(getRadix()) ?: return "" //means it's not a valid number
        return getNumberRepresentation()
    }

    private fun getRadix() =
        when (topPosition) {
            15 -> 32
            16 -> 36
            else -> topPosition + 2
        }

    private fun getNumberRepresentation(): String {
        val topBaseRepresentation = computeValues(topPosition)
        val bottomBaseRepresentation = computeValues(bottomPosition)
        //top position is the one currently in focus
        val ucInputString = inputString.toUpperCase(Locale.getDefault())
        val stringIsValid = ucInputString.all { it in topBaseRepresentation }
        if (!stringIsValid) return ""
        return baseToOtherBase(ucInputString, topBaseRepresentation, bottomBaseRepresentation)
    }

    private val fullRepresentation =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toUpperCase(Locale.getDefault())

    private fun computeValues(int: Int): String {
        return when (int) {
            15 -> fullRepresentation.substring(0 until 32)
            16 -> fullRepresentation
            else -> fullRepresentation.substring(0 until int + 2)
        }
    }

    // for integers
    private fun baseTenToOther(int: Int, base: String): String =
        buildString {
            val length = base.length
            var number = int
            while (number > 0) {
                insert(0, base[number % length])
                number /= length
            }
        }

    private fun otherBaseToBaseTen(number: CharSequence, base: CharSequence): Int {
        val length = base.length
        var numberInBaseTen = 0
        for (i in number)
            numberInBaseTen =
                length * numberInBaseTen + base.subSequence(0 until length).indexOf(i)
        return numberInBaseTen
    }

    private fun baseToOtherBase(digits: CharSequence, baseFrom: String, baseTo: String): String =
        baseTenToOther(otherBaseToBaseTen(digits, baseFrom), baseTo)
}