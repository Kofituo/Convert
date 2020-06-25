package com.example.unitconverter.subclasses

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewUpdater<T> : DiffUtil.Callback() {

    lateinit var oldList: List<T>
    lateinit var newList: List<T?>
    var comparator: Comparator<T>? = null
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        comparator?.apply {
            return compare(oldList[oldItemPosition], newList[newItemPosition]) == 0
        }
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    fun apply(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        DiffUtil.calculateDiff(this).dispatchUpdatesTo(adapter)
    }
}
