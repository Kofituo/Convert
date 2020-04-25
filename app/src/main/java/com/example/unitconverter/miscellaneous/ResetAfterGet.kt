package com.example.unitconverter.miscellaneous

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ResetAfterGet<T>(initialValue: T, private val reset: T) :
    ReadWriteProperty<Any?, T> {

    var value = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val previous = value
        value = reset
        return previous
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    companion object {
        fun <T> resetAfterGet(initialValue: T, resetValue: T) =
            ResetAfterGet(initialValue, resetValue)
    }
}