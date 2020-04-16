@file:Suppress("NOTHING_TO_INLINE")

package com.example.unitconverter.miscellaneous

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

//watch
//contracts are used
@OptIn(ExperimentalContracts::class)
inline fun <T> T.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

@OptIn(ExperimentalContracts::class)
inline fun <T> T.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

/**
 * This.isNotNull() && this.isNotEmpty()
 * */
@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.hasValue(): Boolean {
    contract {
        returns(true) implies (this@hasValue != null)
    }
    return this.isNotNull() && this.isNotEmpty()
}

/**
 * Same as non null asserted call
 * */
/*
@OptIn(ExperimentalContracts::class)
inline fun <reified T> value(any: T?): T {
    contract {
        returns() implies (any != null)
    }
    return any!!
}
*/
