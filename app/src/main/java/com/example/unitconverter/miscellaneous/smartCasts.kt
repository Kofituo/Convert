@file:Suppress("NOTHING_TO_INLINE")

package com.example.unitconverter.miscellaneous

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

//watch contracts are used
@UseExperimental(ExperimentalContracts::class)
inline fun <T> T.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

@UseExperimental(ExperimentalContracts::class)
inline fun <T> T.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }
    return this == null
}