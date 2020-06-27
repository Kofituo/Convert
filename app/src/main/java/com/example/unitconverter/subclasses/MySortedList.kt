package com.example.unitconverter.subclasses

import androidx.recyclerview.widget.SortedList

class MySortedList<T>(val sortedList: SortedList<T>) : MutableList<T> {

    override val size: Int
        get() = sortedList.size()

    override fun contains(element: T): Boolean = false

    override fun containsAll(elements: Collection<T>): Boolean =
        elements.all { it in this }

    override fun isEmpty(): Boolean = sortedList.size() == 0

    override fun iterator(): MutableIterator<T> = TODO()

    override fun removeAll(elements: Collection<T>): Boolean {
        val initialSize = sortedList.size()
        elements.forEach { sortedList.remove(it) }
        return initialSize != sortedList.size()
    }

    override fun retainAll(elements: Collection<T>): Boolean =
        TODO()

    override fun add(element: T): Boolean {
        val initialSize = sortedList.size()
        return sortedList.add(element).let { initialSize != sortedList.size() }
    }

    override fun addAll(elements: Collection<T>): Boolean =
        sortedList.addAll(elements).let { true }

    override fun clear() = sortedList.clear()

    override fun remove(element: T): Boolean {
        val initialSize = sortedList.size()
        return sortedList.remove(element).let { initialSize != sortedList.size() }
    }

    override fun get(index: Int): T =
        sortedList[index]

    override fun indexOf(element: T): Int =
        sortedList.indexOf(element)

    override fun lastIndexOf(element: T): Int =
        sortedList.indexOf(element)

    override fun add(index: Int, element: T) {
        sortedList.add(element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val initialSize = sortedList.size()
        sortedList.addAll(elements)
        return initialSize != sortedList.size()
    }

    override fun listIterator(): MutableListIterator<T> {
        TODO()
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        TODO()
    }

    override fun removeAt(index: Int): T =
        sortedList.removeItemAt(index)


    override fun set(index: Int, element: T): T {
        TODO()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        TODO()
    }
}