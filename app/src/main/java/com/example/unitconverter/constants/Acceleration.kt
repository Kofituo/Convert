package com.example.unitconverter.constants

import android.util.SparseIntArray
import com.example.unitconverter.builders.buildSparseIntArray

object Acceleration {

    fun amongSeconds(intRange: IntRange): SparseIntArray {
        val iterator = intRange.iterator()
        return buildSparseIntArray(3) {
            append(iterator.next(), 0) //second
            append(iterator.next(), 1 * 2) // minute
            append(iterator.nextInt(), 2 * 2) // hours
        }
    }

    fun hoursMap(intRange: IntRange): SparseIntArray {
        val iterator = intRange.iterator()
        return buildSparseIntArray(3) {
            append(iterator.nextInt(), 3600 * 24) //second
            append(iterator.nextInt(), 60 * 24) // minute
            append(iterator.nextInt(), 24) // hours
        }
    }

}