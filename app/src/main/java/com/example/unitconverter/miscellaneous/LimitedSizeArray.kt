package com.example.unitconverter.miscellaneous

import android.util.Log

class LimitedSizeArray<T>(val maxSize: Int) : MutableCollection<T> {
    private val arrayList = ArrayList<T>(maxSize)

    override val size: Int
        get() = arrayList.size

    override fun contains(element: T): Boolean = element in arrayList

    override fun containsAll(elements: Collection<T>): Boolean {
        elements.forEach {
            if (it !in arrayList)
                return false
        }
        return true
    }

    override fun isEmpty(): Boolean = arrayList.isEmpty()

    override fun iterator(): MutableIterator<T> = arrayList.iterator()

    override fun add(element: T): Boolean {
        if (size == maxSize) return false
        return arrayList.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return when {
            arrayList.size == maxSize || elements.size > maxSize -> false
            arrayList.size + elements.size > maxSize -> false
            else -> arrayList.addAll(elements)
        }
    }

    override fun clear() = arrayList.clear()

    override fun remove(element: T): Boolean = arrayList.remove(element)

    override fun removeAll(elements: Collection<T>): Boolean = arrayList.removeAll(elements)

    override fun retainAll(elements: Collection<T>): Boolean = arrayList.retainAll(elements)
}