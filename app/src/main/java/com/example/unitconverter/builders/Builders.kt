@file:Suppress("NOTHING_TO_INLINE")

package com.example.unitconverter.builders

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.SparseIntArray
import androidx.constraintlayout.widget.ConstraintSet
import com.example.unitconverter.subclasses.KotlinConstraintSet

/* Collection builders*/
/**
 * Creates a new mutable list of [T] and applies [block] to it with an initial [capacity]
 * */
inline fun <T> buildMutableList(capacity: Int, block: MutableList<T>.() -> Unit): MutableList<T> =
    ArrayList<T>(capacity).apply(block)

/**
 * Creates a new mutable list of [T] and applies [block] to it with an initial capacity of 10
 * */
inline fun <T> buildMutableList(block: MutableList<T>.() -> Unit): MutableList<T> =
    mutableListOf<T>().apply(block)

/**
 * Creates new [ArrayList] with initial [capacity]
 * */
inline fun <T> arrayListOf(capacity: Int): ArrayList<T> = ArrayList(capacity)
/*
 *Creates new mutable map<[K],[V]> and applies [action] to it
 * For use with maps with keys less than 16
 * */
/*
inline fun <K, V> buildMutableMap(action: MutableMap<K, V>.() -> Unit): MutableMap<K, V> =
    mutableMapOf<K, V>().apply(action)
*/

/**
 * Creates new mutable map<[K],[V]> and applies [action] to it.
 *
 * Default [capacity] = 30
 * */
inline fun <K, V> buildMutableMap(capacity: Int = 30, action: MutableMap<K, V>.() -> Unit) =
    LinkedHashMap<K, V>(capacity).apply(action)

data class MapValue<K, V>(var key: K? = null, var value: V? = null)

inline fun <K, V> MutableMap<K, V>.put(block: MapValue<K, V>.() -> Unit) =
    MapValue<K, V>().apply(block).run {
        put(key!!, value!!)
    }

/*End of collection builders*/

/* Intent builder*/
/**
 * Creates a new [Intent] and applies [block] to it
 * */
inline fun <reified T> buildIntent(context: Context, block: Intent.() -> Unit) =
    Intent(context, T::class.java).apply(block)


/* Intent builder*/
/**
 * Creates a new [Intent] and applies [block] to it
 *
 * Returns true
 * */
inline fun <reified T> Activity.buildIntent(block: Intent.() -> Unit) =
    true.also {
        Intent(this, T::class.java).apply(block)
    }

/**
 * Creates a new [Intent] and applies [builder] to it
 *
 * Returns true
 * */
inline fun intentBuilder(builder: Intent.() -> Unit) =
    true.also {
        Intent().apply(builder)
    }

/*Constraint set*/
/**
 * Creates a new [ConstraintSet] and applies the [block] to it
 * */
inline fun buildConstraintSet(block: KotlinConstraintSet.() -> Unit) =
    KotlinConstraintSet().apply(block)

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
