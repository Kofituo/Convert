package com.example.unitconverter.subclasses

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.unitconverter.RecyclerDataClass

class SortedRecyclerDataCallback(
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val comparator: Comparator<RecyclerDataClass>
) :
    SortedList.Callback<RecyclerDataClass>() {
    override fun areItemsTheSame(item1: RecyclerDataClass, item2: RecyclerDataClass) =
        item1.quantity == item2.quantity &&
                item1.correspondingUnit == item2.correspondingUnit

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