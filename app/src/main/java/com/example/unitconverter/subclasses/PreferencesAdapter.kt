@file:Suppress("KDocUnresolvedReference")

package com.example.unitconverter.subclasses

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.FlattenMap
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.inflate

/**
 * [dataSet] has keys as the header and the values as children
 * */
class PreferencesAdapter(private val dataSet: Map<String, Collection<PreferenceData>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), GroupViewHolder.GroupClickListener,
    ChildViewHolder.ChildClickListener, DecimalPlaceHolder.SliderListener {

    var color: Int? = null
    private var visibleItemsPerGroup = LinkedHashMap<Int, Int>(dataSet.size)

    /**
     * At start say the first 5 views are all groups
     * means position is from 0 to 4
     * When the user clicks a group, [notifyItemRangeInserted]
     * would be called and it would push the next group ,giving it a new position.
     * @example lets say we have notation (0), dec sep (1), group sep (2), decimal place (3)
     * when we tap on notation which has 3 child views the recycler view becomes
     * notation (0) , ch1 (1) ,ch2 (2) , ch3 (3) , dec sep (4), group sep (5) , dec place (6)
     * */

    init {
        //fill map
        var index = 0
        dataSet.map {
            visibleItemsPerGroup[index++] = it.value.size
        }
        visibleItemsPerGroup.apply { put(index - 1, 1) }//for the last element
    }

    //initially it's only the headers
    private var numberOfVisibleItems = dataSet.size

    override fun getItemViewType(position: Int): Int {
        //Log.e("map","position $position ${FlattenMap.getType(dataSetCopy, position)}")
        return FlattenMap.getType(dataSetCopy, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder
        //Log.e("view type","$viewType $itemCount")
        when (viewType) {
            FlattenMap.GROUP -> {
                //for the group
                val view = inflater.inflate {
                    resourceId = R.layout.preferences_group
                    root = parent
                }
                viewHolder =
                    GroupViewHolder(view, color!!)
                        .apply { setOnGroupClickListener(this@PreferencesAdapter) }
            }
            FlattenMap.CHILD -> {
                //for the child
                val view = inflater.inflate {
                    resourceId = R.layout.notation
                    root = parent
                }
                viewHolder =
                    ChildViewHolder(view, color!!)
                        .apply { setOnChildClickedListener(this@PreferencesAdapter) }
            }
            FlattenMap.UNSPECIFIED -> {
                //for the slider
                val view = inflater.inflate {
                    resourceId = R.layout.decimal_place
                    root = parent
                }
                viewHolder =
                    DecimalPlaceHolder(view, color!!)
                        .apply { setSliderListener(this@PreferencesAdapter) }
            }
            else -> TODO()
        }
        return viewHolder
    }

    override fun getItemCount(): Int = numberOfVisibleItems

    lateinit var context: Context

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is GroupViewHolder) {
            val pos = holder.adapterPosition
            FlattenMap.getGroupPosition(dataSetCopy, pos)?.let {
                if (groupsExpanded[it])
                    holder.rotateArrow0T180()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Log.e("bind", "$holder  pos $position   ${FlattenMap.getType(dataSetCopy, position)}")
        when (FlattenMap.getType(dataSetCopy, position)) {
            FlattenMap.GROUP -> {
                //bind for the groups
                val originalGroup =
                    FlattenMap.getGroupPosition(dataSetCopy, position) ?: error("should'nt be null")
                originalGroup.let {
                    (holder as GroupViewHolder).header.text = dataSetCopy.keyAt(it)
                }
            }
            FlattenMap.CHILD -> {
                (holder as ChildViewHolder)
                    .titleButton.apply {
                        text = FlattenMap.getChildText(dataSetCopy, position).string
                    }
            }
            FlattenMap.UNSPECIFIED -> {
                //(holder as DecimalPlaceHolder)
                Log.e("UNSPECIFIED", "UNSPECIFIED")
            }
        }
    }

    private val headerToIndex =
        LinkedHashMap<Int, String>(dataSet.size).apply {
            var index = 0
            dataSet.forEach {
                put(index++, it.key)
            }
        }


    private val dataSetCopy =
        ArrayMap<String, MutableList<PreferenceData>>(dataSet.size).apply {
            dataSet.forEach {
                put(it.key, mutableListOf())
            }
        }

    private val groupsExpanded = SparseBooleanArray(dataSet.size).apply {
        var index = 0
        dataSet.forEach { _ ->
            append(index++, false)
        }
    }

    private fun expand(groupPosition: Int, originalPosition: Int) {
        val visibleItems = visibleItemsPerGroup[groupPosition] ?: error("check code $groupPosition")
        val newMap = LinkedHashMap<Int, Int>(visibleItemsPerGroup.size)
        for ((position, numberOfChildren) in visibleItemsPerGroup.toMap()) {
            /**only the views after the clicked group would be affected by the collapse*/
            if (position > groupPosition)
                newMap[position + visibleItems] = numberOfChildren
            else newMap[position] = numberOfChildren
        }
        visibleItemsPerGroup = newMap
        val previousList = FlattenMap.convertMapToList(dataSetCopy)
        val title = headerToIndex[originalPosition]
        dataSet[headerToIndex[originalPosition]]?.let {
            //Log.e("str", "$it")
            dataSetCopy[title]?.addAll(it)
        }
        numberOfVisibleItems += visibleItems
        RecyclerViewUpdater<Int>().apply {
            oldList = previousList
            newList = FlattenMap.convertMapToNullList(previousList, groupPosition, visibleItems)
            apply(this@PreferencesAdapter)
        }
    }

    /**@param position refer to the position clicked
     * notify that from after the header to items in itemsInGroup has been remove
     * Eventually update [visibleItemsPerGroup]
     * when we tap on notation which has 3 child views the recycler view becomes
     * notation (0) , ch1 (1) ,ch2 (2) , ch3 (3) , dec sep (4), group sep (5) , dec place (6)
     * */
    private fun collapse(position: Int, initialPosition: Int) {
        val itemsInGroup = visibleItemsPerGroup[position] ?: error("check code $position")
        val newMap = LinkedHashMap<Int, Int>(visibleItemsPerGroup.size)
        for ((groupPosition, numberOfChildren) in visibleItemsPerGroup) {
            /**only the views after the clicked group would be affected by the collapse*/
            if (groupPosition > position) {
                newMap[groupPosition - itemsInGroup] = numberOfChildren
            } else newMap[groupPosition] = numberOfChildren
        }
        visibleItemsPerGroup = newMap

        val title = headerToIndex[initialPosition]
        Log.e("before", "$dataSetCopy  $title")
        dataSetCopy[title]?.clear()
        Log.e("after", "$dataSetCopy")
        numberOfVisibleItems -= itemsInGroup
        RecyclerViewUpdater<Int?>().apply {
            val dataSetToList = FlattenMap.convertMapToList(dataSetCopy)
            Log.e("dat after", "$dataSetToList")
            newList = dataSetToList
            oldList = FlattenMap.convertMapToNullList(
                dataSetToList,
                position,
                itemsInGroup
            )
            Log.e("da befre", "$oldList")
            apply(this@PreferencesAdapter)
        }
    }

    /**
     * Called just before an expansion or collapse occurs
     * when a group is tapped,if it's expanded collapse it by removing the child views
     * else add views and call [notifyItemRangeInserted]
     *
     * @param  position refers to the position clicked
     * */
    override fun onGroupClick(position: Int, holder: GroupViewHolder) {
        val initialPosition =
            FlattenMap.getGroupPosition(dataSetCopy, position) ?: error("check code $position")
        if (!groupsExpanded[initialPosition]) {
            //expand it
            groupsExpanded.put(initialPosition, true)
            holder.rotateArrow0T180()
            expand(position, initialPosition)
        } else {
            groupsExpanded.put(initialPosition, false)
            holder.rotateArrow180To0()
            collapse(position, initialPosition)
        }
    }

    override fun onChildClicked(position: Int) {
    }

    override fun onTrackChanged(value: Float) {
    }
}