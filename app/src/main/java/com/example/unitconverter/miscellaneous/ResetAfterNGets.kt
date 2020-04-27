package com.example.unitconverter.miscellaneous

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ResetAfterNGets<T>(initialValue: T, private val reset: T, afterNGets: Int) :
    ReadWriteProperty<Any?, T> {
    private val afterNGets =
        if (afterNGets < 1) error("After Number Of Gets Should Be Greater Than One")
        else afterNGets
    private var count = 0
    private var value = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        count++
        val previous = value
        if (count == afterNGets)
            value = reset
        return previous
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        count = 0
    }

    companion object {
        fun <T> resetAfterGet(initialValue: T, resetValue: T) =
            ResetAfterNGets(initialValue, resetValue, 1)

        fun <T> resetAfter2Gets(initialValue: T, resetValue: T) =
            ResetAfterNGets(initialValue, resetValue, 2)
    }
}