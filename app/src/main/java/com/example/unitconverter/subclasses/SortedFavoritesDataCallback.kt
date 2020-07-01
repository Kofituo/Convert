package com.example.unitconverter.subclasses

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList

class SortedFavoritesDataCallback(
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val comparator: Comparator<FavouritesData>
) :
    SortedList.Callback<FavouritesData>() {
    override fun areItemsTheSame(item1: FavouritesData, item2: FavouritesData): Boolean =
        item1.cardName == item2.cardName && item1.cardId == item2.cardId

    override fun onMoved(fromPosition: Int, toPosition: Int) =
        adapter.notifyItemMoved(fromPosition, toPosition)

    override fun onChanged(position: Int, count: Int) =
        adapter.notifyItemRangeChanged(position, count)

    override fun onInserted(position: Int, count: Int) =
        adapter.notifyItemRangeInserted(position, count)

    override fun onRemoved(position: Int, count: Int) =
        adapter.notifyItemRangeRemoved(position, count)

    override fun compare(o1: FavouritesData?, o2: FavouritesData?): Int =
        comparator.compare(o1, o2)


    override fun areContentsTheSame(oldItem: FavouritesData, newItem: FavouritesData): Boolean =
        oldItem.cardName == newItem.cardName
                && oldItem.cardId == newItem.cardId
                && oldItem.drawableId == newItem.drawableId

}