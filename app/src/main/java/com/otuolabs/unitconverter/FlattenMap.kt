package com.otuolabs.unitconverter

object FlattenMap {

    const val GROUP = 0
    const val CHILD = 1
    const val UNSPECIFIED = 2
    private const val UNKNOWN = -1

/*
    fun <T, U> getTypeForSortedList(map: Map<U, SortedList<T>>, position: Int): Int {
        if (position < 0) return UNKNOWN
        val groups = ArrayList<Int>(map.size)
        var index = 0
        for ((_, list) in map) {
            groups.add(index)
            index += list.size() + 1
        }
        if (position >= index) return UNSPECIFIED// for the last one
        return if (position in groups) GROUP else CHILD
    }
*/

    fun <T, U> getType(map: Map<U, Collection<T>>, position: Int): Int {
        if (position < 0) return UNKNOWN
        val groups = ArrayList<Int>(map.size)
        var index = 0
        for ((_, list) in map) {
            groups.add(index)
            index += list.size + 1
        }
        if (position >= index) return UNSPECIFIED// for the last one
        return if (position in groups) GROUP else CHILD
    }

    /**
     * Get original group position
     * */
    fun <T, J> getGroupPosition(map: Map<J, Collection<T>>, position: Int): Int? {
        val groups = LinkedHashMap<Int, Int>(map.size)
        var mapIndex = 0
        var index = 0
        for ((_, list) in map) {
            groups[index] = mapIndex++
            index += list.size + 1
        }
        return groups[position]
    }

    /*fun <T, J> getGroupPositionForSorted(map: Map<J, SortedList<T>>, position: Int): Int? {
        val groups = LinkedHashMap<Int, Int>(map.size)
        var mapIndex = 0
        var index = 0
        for ((_, list) in map) {
            groups[index] = mapIndex++
            index += list.size() + 1
        }
        return groups[position]
    }*/

/*

    fun <J, K> getGroups(map: Map<J, Collection<K>>): ArrayList<Int> {
        val groups = ArrayList<Int>(map.size)
        var index = 0
        for ((_, list) in map) {
            groups.add(index)
            index += list.size + 1
        }
        return groups
    }
*/

    /*private fun <T, N> getChildData(map: Map<N, Collection<T>>, position: Int): T {
        var index = 0
        for ((_, list) in map) {
            index++
            list.forEach {
                if (position == index)
                    return it
                index++
            }
        }
        error("this is not a child $position  $map")
    }*/

    //faster than the above version since it does'nt iterate all the list
    fun <T, N> getChildData(map: Map<N, Collection<T>>, position: Int): T {
        require(position >= 0)
        var index = -1
        for ((_, list) in map) {
            val listSize = list.size
            index += listSize + 1 //plus 1 for the header
            if (index >= position) {
                val positionInList =
                    position - (index - listSize) - 1 //minus 1 because the size is always 1 ahead the index
                return if (list is List) list[positionInList] else break
            }
        }
        error("this is not a child position $position  map $map")
    }

    fun <T, S> convertMapToList(map: Map<S, Collection<T>>, startIndex: Int = 0): ArrayList<Int> {
        val array = ArrayList<Int>(map.size + 8)
        var index = startIndex
        for ((_, list) in map) {
            array.add(index++)
            list.forEach { _ ->
                array.add(index++)
            }
        }
        return array
    }

    fun <T> addNullsToList(
        oldList: Collection<T>,
        changedIndex: Int,
        numberOfNewItems: Int
    ): ArrayList<T?> {
        val array = ArrayList<T?>(oldList.size + 4)
        for (i in oldList) {
            array.add(i)
            if (i == changedIndex) (0 until numberOfNewItems).forEach { _ -> array.add(null) }
        }
        return array
    }
}