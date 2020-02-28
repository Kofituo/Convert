package com.example.unitconverter.builders

import android.content.Context
import android.content.Intent
import java.math.BigDecimal

/**
 * Collection builders
 * */
/**
 * Creates a new mutable list of [T] and applies [block] to it
 * */
inline fun <T> buildMutableList(block: MutableList<T>.() -> Unit) =
    mutableListOf<T>().apply(block)

/**
 * Creates new mutable map<[K],[V]> and applies [action] to it
 * */
inline fun <K, V> buildMutableMap(action: MutableMap<K, V>.() -> Unit) =
    mutableMapOf<K, V>().apply(action)

/*End of collection builders*/

/* Intent builder*/
/**
 * Creates a new [Intent] and applies [block] to it
 * */
inline fun <T> buildIntent(context: Context, clazz: Class<T>, block: Intent.() -> Unit) =
    Intent(context, clazz).apply(block)

/*Big decimal*/
/**
 * Applies [action] to [BigDecimal] and returns the last statement
 * */
inline fun <T> BigDecimal.operate(action: BigDecimal.() -> T) =
    with(this, block = action)