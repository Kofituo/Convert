package com.example.unitconverter.miscellaneous

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.builders.buildMutableList
import com.example.unitconverter.subclasses.MyAdapter

class SearchTextChangeListener(
    private val recyclerView: RecyclerView,
    private val adapter: MyAdapter,
    private val dataSet: MutableList<RecyclerDataClass>
) : TextWatcher {
    /**
     * If true means it has informed the recycler view to use the filtered data set
     * */
    private var called = false

    override fun afterTextChanged(s: Editable) {
        val filteredList = filter(dataSet, s)
        adapter.replaceAll(filteredList) // so the list would be updated to work with item count
        /**
         * scroll to the selected position if there's no text
         * else scroll to the first position
         */
        if (s.isEmpty()) {
            adapter.useFilteredList = false //tells the recycler view to use the original list
            called = false
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
            if (adapter.lastPosition != -1)
                recyclerView.smoothScrollToPosition(adapter.lastPosition)
        } else
            recyclerView.smoothScrollToPosition(0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        /**
         * It informs the recycler view to use the filtered data set
         * */
        adapter.useFilteredList = true
        //inform the recycler view to use the filtered collection
        if (!called) adapter.notifyItemRangeChanged(0, adapter.itemCount)
        called = true
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun filter(
        dataSet: MutableList<RecyclerDataClass>,
        searchText: CharSequence
    ): MutableList<RecyclerDataClass> {
        if (searchText.isBlank()) return dataSet //fast return
        val mainText = searchText.trim()

        return buildMutableList {
            for (i in dataSet) {
                val text = i.quantity
                val unit = i.correspondingUnit
                if (text.contains(mainText, ignoreCase = true) ||
                    unit.contains(mainText, ignoreCase = true)
                ) add(i)
            }
        }
    }
}