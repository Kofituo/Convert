package com.example.unitconverter.functions

import android.util.SparseIntArray
import com.example.unitconverter.constants.Time
import com.example.unitconverter.subclasses.Positions

class Time(override val positions: Positions) : ConstantsAbstractClass() {
    override fun getText(): String = amongSeconds() ?: ""

    private fun amongSeconds(): String? {
        // means its amongst the seconds family
        if (topPosition in 0..16 && bottomPosition in 0..16) {
            prefix {
                top = it[topPosition]
                bottom = it[bottomPosition]
                return prefixMultiplication(inputString)
            }
        }
        return null
    }

    /*Helper fun */
    private inline fun time(block: Time.() -> Unit) = Time.apply(block)

    private inline fun prefix(block: (SparseIntArray) -> Unit) = Time.buildPrefix().also(block)
}