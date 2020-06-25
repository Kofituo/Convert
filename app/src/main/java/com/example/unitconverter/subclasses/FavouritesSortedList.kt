package com.example.unitconverter.subclasses

import androidx.recyclerview.widget.SortedList


class FavouritesSortedList(private val adapter: FavouritesAdapter) :
    SortedList.Callback<FavouritesData>() {

    private val comparator = Comparator { o1: FavouritesData, o2 ->
        o1.cardName!!.compareTo(o2.cardName!!)
    }

    override fun areItemsTheSame(item1: FavouritesData, item2: FavouritesData) =
        item1.cardName == item2.cardName &&
                item1.drawableId == item2.drawableId

    override fun onMoved(fromPosition: Int, toPosition: Int) =
        adapter.notifyItemMoved(fromPosition, toPosition)

    override fun onChanged(position: Int, count: Int) =
        adapter.notifyItemRangeChanged(position, count)

    override fun onInserted(position: Int, count: Int) =
        adapter.notifyItemRangeInserted(position, count)

    override fun onRemoved(position: Int, count: Int) =
        adapter.notifyItemRangeRemoved(position, count)

    override fun compare(o1: FavouritesData, o2: FavouritesData) =
        comparator.compare(o1, o2)

    override fun areContentsTheSame(
        oldItem: FavouritesData?,
        newItem: FavouritesData?
    ) =
        oldItem?.cardName == newItem?.cardName &&
                oldItem?.cardId == newItem?.cardId
                && oldItem?.drawableId == newItem?.drawableId
}