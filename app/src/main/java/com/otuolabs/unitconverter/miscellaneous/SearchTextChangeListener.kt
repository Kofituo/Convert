package com.otuolabs.unitconverter.miscellaneous

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.otuolabs.unitconverter.RecyclerDataClass
import com.otuolabs.unitconverter.Utils.containsIgnoreCase
import com.otuolabs.unitconverter.builders.addAll
import com.otuolabs.unitconverter.subclasses.MyAdapter

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
        val filteredList = filter<MutableList<RecyclerDataClass>>(dataSet, s)
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
            recyclerView.scrollToPosition(0)
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

    companion object {
        inline fun <reified T : MutableCollection<RecyclerDataClass>> filter(
            dataSet: List<RecyclerDataClass>,
            searchText: CharSequence,
            returnList: T = ArrayList<RecyclerDataClass>(dataSet.size) as T
        ): T {
            if (searchText.isBlank())
                return returnList.apply { addAll { dataSet } }
            val mainText = searchText.trim()

            return returnList.apply {
                for (i in dataSet) {
                    val text = i.topText
                    val unit = i.bottomText
                    if (unit.containsIgnoreCase(mainText) ||
                        text.containsIgnoreCase(mainText)
                    ) add(i)
                }
            }
        }
    }
}
//just in case
/*recyclerView
                .apply {
                    (layoutManager as? LinearLayoutManager)
                        ?.scrollToPositionWithOffset(0, 0)
                }*/