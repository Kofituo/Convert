package com.example.unitconverter.miscellaneous

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.subclasses.MyAdapter
import java.util.*

class SearchTextChangeListener(
    private val recyclerView: RecyclerView,
    private val adapter: MyAdapter,
    private val dataSet: MutableList<RecyclerDataClass>
) : TextWatcher {

    private var called = false
    override fun afterTextChanged(s: Editable) {
        val filteredList = filter(dataSet, s.toString())
        adapter.replaceAll(filteredList) // so the list would be updated to work with item count
        /**
         * scroll to the selected position if there's no text
         * else scroll to the first position
         */
        if (s.isEmpty()) {
            adapter.boolean = false
            called = false
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
            if (adapter.lastPosition != -1)
                recyclerView.smoothScrollToPosition(adapter.lastPosition)
        } else
            recyclerView.smoothScrollToPosition(0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        adapter.boolean = true
        if (!called) adapter.notifyItemRangeChanged(0, adapter.itemCount)
        called = true
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    private fun filter(
        dataSet: MutableList<RecyclerDataClass>,
        searchText: String
    ): MutableList<RecyclerDataClass> {
        val locale = Locale.getDefault()
        val mainText = searchText.trim().toLowerCase(locale)
        return mutableListOf<RecyclerDataClass>().apply {
            for (i in dataSet) {
                val text = i.quantity.toLowerCase(locale)
                val unit = (i.correspondingUnit.toString()).toLowerCase(locale)
                if (text.contains(mainText) || unit.contains(mainText)) add(i)
            }
        }
    }
}