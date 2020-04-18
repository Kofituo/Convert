package com.example.unitconverter.miscellaneous

import java.util.*


/**
 * Inserts the list at the front of the array
 * */
fun <T> ArrayDeque<T>.offerFirst(list: List<T>) {
    list.forEach {
        offerFirst(it)
    }
}

/**
 * Puts the list at the front of the array
 * */
fun <T> ArrayDeque<T>.offerFirst(arrayDeque: ArrayDeque<T>) {
    arrayDeque.descendingIterator().forEach {
        if (it in this)
            remove(it)
        offerFirst(it)
    }
}

/**
 * Puts the list at the end of the array
 * */
fun <T> ArrayDeque<T>.offerLast(array: Collection<T>): ArrayDeque<T> {
    array.forEach {
        if (it !in this)
            offer(it)
    }
    return this
}