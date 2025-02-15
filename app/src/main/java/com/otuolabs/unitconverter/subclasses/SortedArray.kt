package com.otuolabs.unitconverter.subclasses

import android.os.Build
import androidx.annotation.RequiresApi
import com.otuolabs.unitconverter.miscellaneous.isNull
import java.util.*
import java.util.function.Consumer
import java.util.function.UnaryOperator
import kotlin.collections.HashMap


/**
 * Since binary Sort on every add operation takes Considerable time,
 * It sorts at sort only when it hasn't already been sorted.
 * It's backed by a map to prevent duplication
 * */
class SortedArray<T>(comparator: Comparator<T>, initialCapacity: Int = 10) :
        ArrayList<T>(initialCapacity) {

    private var comparator: Comparator<T>? = comparator
    private var isSorted = true //initially it's sorted
    private val map: MutableMap<T, T>

    init {
        map = HashMap(initialCapacity)
    }

    override fun add(element: T): Boolean {
        if (map[element].isNull()) {
            //add it
            isSorted = false
            map[element] = element
            return super.add(element)
        }
        return false
    }

    override fun add(index: Int, element: T) {
        if (map[element].isNull()) {
            isSorted = false
            map[element] = element
            return super.add(index, element)
        }
    }

    override fun get(index: Int): T {
        checkSorted()
        return super.get(index)
    }

    override fun iterator(): MutableIterator<T> {
        checkSorted()
        return super.iterator()
    }

    override fun listIterator(): MutableListIterator<T> {
        checkSorted()
        return super.listIterator()
    }

    override fun lastIndexOf(element: T): Int {
        checkSorted()
        return super.lastIndexOf(element)
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        checkSorted()
        return super.listIterator(index)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        removeAllFromSet(elements)
        return super.removeAll(elements)
    }

    //copied
    private fun removeAllFromSet(collection: Collection<T>): Boolean {
        var modified = false
        if (size > collection.size) {
            val i = collection.iterator()
            while (i.hasNext()) {
                modified = modified or remove(i.next())
            }
        } else {
            val i: MutableIterator<T> = map.keys.iterator()
            while (i.hasNext()) {
                if (collection.contains(i.next())) {
                    i.remove()
                    modified = true
                }
            }
        }
        return modified
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun forEach(action: Consumer<in T>) {
        checkSorted()
        super.forEach(action)
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        checkSorted()
        (fromIndex until toIndex).forEach {
            map.remove(get(it))
        }
        super.removeRange(fromIndex, toIndex)
    }

    override fun toArray(): Array<Any> {
        checkSorted()
        return super.toArray()
    }

    override fun <T : Any?> toArray(a: Array<T>): Array<T> {
        checkSorted()
        return super.toArray(a)
    }

    override fun spliterator(): Spliterator<T> {
        checkSorted()
        return super.spliterator()
    }

    override fun set(index: Int, element: T): T {
        checkSorted()
        return super.set(index, element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        checkSorted()
        return super.subList(fromIndex, toIndex)
    }

    override fun removeAt(index: Int): T {
        checkSorted()
        map.remove(get(index))
        return super.removeAt(index)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun replaceAll(operator: UnaryOperator<T>) {
        super.replaceAll(operator)
        TODO()
    }

    override fun remove(element: T): Boolean {
        map.remove(element)
        return super.remove(element)
    }

    override fun clone(): Any {
        checkSorted()
        return super.clone()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (map.isEmpty()) {
            isSorted = false
            elements.forEach { map[it] = it }
            return super.addAll(elements)
        }
        elements.forEach {
            if (map[it].isNull()) {
                add(it)
                map[it] = it
            }
        }
        return true
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (map.isEmpty()) {
            isSorted = false
            return super.addAll(index, elements)
        }
        TODO()
    }

    override fun contains(element: T): Boolean {
        checkSorted()
        return Collections.binarySearch(this, element, comparator) > -1
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun checkSorted() {
        if (!isSorted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                super.sort(comparator)
            else
                Collections.sort(this, comparator)
            isSorted = true
        }
    }

    override fun indexOf(element: T): Int {
        checkSorted()
        return Collections.binarySearch(this, element, comparator)
    }
}