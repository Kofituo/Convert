package com.otuolabs.unitconverter.subclasses

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.otuolabs.unitconverter.RecyclerDataClass

class SortedRecyclerDataCallback(
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val comparator: Comparator<RecyclerDataClass>
) :
    SortedList.Callback<RecyclerDataClass>() {
    override fun areItemsTheSame(item1: RecyclerDataClass, item2: RecyclerDataClass) =
        item1.topText == item2.topText &&
                item1.bottomText == item2.bottomText

    override fun onMoved(fromPosition: Int, toPosition: Int) =
        adapter.notifyItemMoved(fromPosition, toPosition)

    override fun onChanged(position: Int, count: Int) =
        adapter.notifyItemRangeChanged(position, count)

    override fun onInserted(position: Int, count: Int) =
        adapter.notifyItemRangeInserted(position, count)

    override fun onRemoved(position: Int, count: Int) =
        adapter.notifyItemRangeRemoved(position, count)

    override fun compare(o1: RecyclerDataClass, o2: RecyclerDataClass) =
        comparator.compare(o1, o2)

    override fun areContentsTheSame(
        oldItem: RecyclerDataClass?,
        newItem: RecyclerDataClass?
    ) = oldItem == newItem
}