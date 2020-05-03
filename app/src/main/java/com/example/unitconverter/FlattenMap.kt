package com.example.unitconverter

object FlattenMap {

    const val GROUP = 0
    const val CHILD = 1
    const val UNSPECIFIED = 2

    fun <T, U> getType(map: Map<U, Collection<T>>, position: Int): Int {
        val groups = ArrayList<Int>(map.size)
        var index = 0
        for ((_, list) in map) {
            groups.add(index)
            index += list.size + 1
        }
        if (position == index) return UNSPECIFIED// for the last one
        return if (position in groups) GROUP else CHILD
    }

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

    fun <T, N> getChildData(map: Map<N, Collection<T>>, position: Int): T {
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
    }

    fun <T, S> convertMapToList(map: Map<S, Collection<T>>): ArrayList<Int> {
        val array = ArrayList<Int>(map.size + 8)
        var index = 0
        for ((_, list) in map) {
            array.add(index++)
            list.forEach { _ ->
                array.add(index++)
            }
        }
        return array
    }

    fun convertMapToNullList(
        oldList: Collection<Int>,
        changedIndex: Int,
        numberOfNewItems: Int
    ): ArrayList<Int?> {
        val array = ArrayList<Int?>(oldList.size + 4)
        for (i in oldList) {
            array.add(i)
            if (i == changedIndex) (0 until numberOfNewItems).forEach { _ -> array.add(null) }
        }
        return array
    }
}