package com.example.unitconverter.subclasses

import java.util.*


class SortedArray<T> : ArrayList<T> {

    var comparator: Comparator<T>? = null

    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor() : super()
    constructor(collection: Collection<T>) : super(collection.size) {
        addAll(collection)
    }

    constructor(collection: Collection<T>, comparator: Comparator<T>) : super(collection.size) {
        this.comparator = comparator
        addAll(collection)
    }

    constructor(comparator: Comparator<T>, initialCapacity: Int = 10) : super(initialCapacity) {
        this.comparator = comparator
    }

    override fun add(element: T): Boolean {
        if (element in this) return false
        val initialSize = size
        val insertionPoint =
            Collections.binarySearch(this, element, comparator)
                .let { if (it > -1) it else (-it) - 1 }
        super.add(insertionPoint, element)
        return size != initialSize
    }

    override fun add(index: Int, element: T) {
        add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var result = false
        /*if (elements.size > 4) {
            result = super.addAll(elements)
            Collections.sort(this, comparator)
        } else {*/
        for (paramT in elements) {
            result = result or add(paramT)
        }
        return result
    }

    override fun contains(element: T): Boolean {
        return Collections.binarySearch(this, element, comparator) > -1
    }
}