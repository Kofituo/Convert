package com.example.unitconverter.builders

import android.content.Context
import android.content.Intent
import android.util.SparseIntArray
import androidx.constraintlayout.widget.ConstraintSet

/* Collection builders*/
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

/*Constraint set*/
/**
 * Creates a new [ConstraintSet] and applies the [block] to it
 * */
inline fun buildConstraintSet(block: ConstraintSet.() -> Unit) =
    ConstraintSet().apply(block)

/*Sparse Int Array*/
/**
 * Creates a new [SparseIntArray] with [capacity]
 * */
inline fun buildSparseIntArray(capacity: Int, builderAction: SparseIntArray.() -> Unit) =
    SparseIntArray(capacity).apply(builderAction)

/**
 * Creates a new [/SparseIntArray] and applies [/builderAction] to it

inline fun buildSparseIntArray(builderAction: SparseIntArray.() -> Unit) =
SparseIntArray().apply(builderAction)

 */