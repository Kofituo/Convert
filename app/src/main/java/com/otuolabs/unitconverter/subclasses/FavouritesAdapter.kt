package com.otuolabs.unitconverter.subclasses

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.inflate
import com.otuolabs.unitconverter.miscellaneous.isNull


class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    var initialSize = -1

    lateinit var unSortedList: MutableList<FavouritesData>

    private var useFilteredList = false

    fun useFilteredList() {
        useFilteredList = true
        //notifyItemRangeChanged(0, itemCount)
    }

    fun useOriginalList() {
        useFilteredList = false
        //notifyItemRangeChanged(0, itemCount)
    }

    val dataSet
        get() = if (useFilteredList) sortedList else unSortedList

    lateinit var sortedList: SortedArray<FavouritesData>

    val currentSize get() = dataSet.size

    lateinit var activity: Activity

    private lateinit var favouritesItem: FavouritesItem

    lateinit var oldList: List<FavouritesData?>
    lateinit var newList: List<FavouritesData?>

    private val cacheDrawable = LinkedHashMap<Int, Drawable>(30)

    private val selectedItems = LinkedHashMap<Int, FavouritesData>(30)

    fun removeItems(): Boolean {
        oldList = ArrayList(unSortedList)
        val result = unSortedList.removeAll(selectedItems.values)
        if (result) selectedItems.clear()
        newList = unSortedList
        DiffUtil.calculateDiff(diffUtil).dispatchUpdatesTo(this)
        return result
    }

    var forceChange = false

    private var enable = true

    fun disableSelection() {
        enable = false
    }

    fun enableSelection() {
        enable = true
    }

    fun endSelection() {
        longClicked = false
    }

    fun startSelection() {
        longClicked = true
        favouritesItem.selectionInitiated()
    }

    /**
     * Selected Items
     * */
    fun getSelectedItemsMap() = selectedItems

    companion object {
        inline fun favouritesAdapter(block: FavouritesAdapter.() -> Unit) =
            FavouritesAdapter().apply(block)
    }

    private var longClicked = false

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val mainTextView: TextView
        val metadataTextView: TextView

        init {
            view.apply {
                imageView = findViewById(R.id.image)
                mainTextView = findViewById(R.id.top_text)
                metadataTextView = findViewById(R.id.metadata)
                setOnClickListener {
                    val pos = this@ViewHolder.adapterPosition
                    if (pos < 0) return@setOnClickListener
                    if (longClicked)
                        selectedItems.apply {
                            if (containsKey(pos)) {
                                remove(pos)
                                ifEmpty { favouritesItem.sizeIsZero() }
                            } else put(pos, dataSet[pos])
                            notifyItemChanged(pos)
                        }
                    else
                        favouritesItem.startActivity(dataSet[pos])
                }
                setOnLongClickListener {
                    if (!enable) return@setOnLongClickListener false
                    favouritesItem.selectionInitiated()
                    longClicked = true
                    performClick()
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate {
                resourceId = R.layout.favorites_recycler_view
                root = parent
            }
        )

    override fun getItemCount(): Int = dataSet.size

    private var rippleDrawable: Drawable? = null
        get() {
            if (field.isNull()) {
                field = activity.getDrawable(R.drawable.ripple)
            }
            return field
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            dataSet[position].apply {
                imageView.setImageDrawable(
                    cacheDrawable[drawableId]
                        ?: activity.resources.getDrawable(drawableId, null)
                            .apply {
                                cacheDrawable[drawableId] = this
                            }
                )
                imageView.contentDescription = topText
                mainTextView.text = topText
                metadataTextView.text = metadata
            }
            ///Log.e("drawable", "${view.background}   $position")
            // had to set it after and not before
            if (longClicked) {
                view.background =
                    if (position in selectedItems) rippleDrawable
                    else activity.getDrawable(R.drawable.init)
            }
            if (forceChange) {
                //it becomes gradient drawable for no reason
                if (view.background !is RippleDrawable)
                    view.background = activity.getDrawable(R.drawable.init)
            }
        }
    }

    private inline val diffUtil
        get() = object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition]?.cardId == newList[newItemPosition]?.cardId

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
        }

    fun setFavouritesItemListener(favouritesItem: FavouritesItem) {
        this.favouritesItem = favouritesItem
    }

    interface FavouritesItem {
        fun startActivity(data: FavouritesData)
        fun selectionInitiated() {}
        fun sizeIsZero() {}
    }
}