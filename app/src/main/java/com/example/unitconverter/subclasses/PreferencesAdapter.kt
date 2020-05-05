package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.FlattenMap
import com.example.unitconverter.R
import com.example.unitconverter.builders.buildMutableMap
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.isNotNull
import com.example.unitconverter.miscellaneous.isNull
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

/**
 * [dataSet] has keys as the header and the values as children
 * */
class PreferencesAdapter(private val dataSet: Map<String, Collection<PreferenceData>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), GroupViewHolder.GroupClickListener,
    ChildViewHolder.ChildClickListener, DecimalPlaceHolder.SliderListener {

    var color: Int? = null
    var visibleItemsPerGroup: MutableMap<Int, Int> = LinkedHashMap(dataSet.size)

    /**
     * At start say the first 5 views are all groups
     * means position is from 0 to 4
     * When the user clicks a group, notifyItemRangeInserted
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
    var numberOfVisibleItems = dataSet.size

    override fun getItemViewType(position: Int): Int {
        //Log.e("map","position $position ${FlattenMap.getType(dataSetCopy, position)}")
        return FlattenMap.getType(dataSetCopy, position)
    }

    private var inflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (inflater.isNull()) inflater = LayoutInflater.from(parent.context)
        val fastInflater = inflater!!
        val viewHolder: RecyclerView.ViewHolder
        //Log.e("view type","$viewType $itemCount")
        when (viewType) {
            FlattenMap.GROUP -> {
                //for the group
                val view = fastInflater.inflate {
                    resourceId = R.layout.preferences_group
                    root = parent
                }
                viewHolder =
                    GroupViewHolder(view, color!!)
                        .apply { setOnGroupClickListener(this@PreferencesAdapter) }
            }
            FlattenMap.CHILD -> {
                //for the child
                val view = fastInflater.inflate {
                    resourceId = R.layout.notation
                    root = parent
                }
                viewHolder =
                    ChildViewHolder(view, color!!)
                        .apply { setOnChildClickedListener(this@PreferencesAdapter) }
            }
            FlattenMap.UNSPECIFIED -> {
                //for the slider
                val view = fastInflater.inflate {
                    resourceId = R.layout.decimal_place
                    root = parent
                }
                viewHolder =
                    DecimalPlaceHolder(view, color!!)
                        .apply {
                            setSliderListener(this@PreferencesAdapter)
                            setSliderInitialValue(sliderValue)
                        }
            }
            else -> TODO()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return numberOfVisibleItems
    }

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
                        val data = FlattenMap.getChildData(dataSetCopy, position)
                        text = data.string
                        myId = data.radioId
                        isChecked = groupToCheckedId[data.groupNumber] == myId
                        if (isChecked)
                            groupToCheckedButton[data.groupNumber] = this
                        /**
                         * initially [groupToCheckedId] has all values null
                         * if the value is null enable that group's buttons
                         * if there's a value, enable only that one and disable the rest
                         * */
                        val mGroupNumber = buttonToMyGroupNumber(myId)
                        if (mGroupNumber.isNull()) {
                            enableRadioButton(this)
                            return
                        }
                        if (mGroupToEnabledID[mGroupNumber]!![myId])
                            enableRadioButton(this)
                        else disableRadioButton(this)

                    }
            }
            FlattenMap.UNSPECIFIED -> {
                //(holder as DecimalPlaceHolder)
                ///Log.e("UNSPECIFIED", "UNSPECIFIED")
            }
        }
    }

    private fun buttonToMyGroupNumber(radioId: Int) =
        when (radioId) {
            4, 8 -> 1
            5, 9 -> 2
            6, 10 -> 3
            else -> null
        }

    private val headerToIndex =
        LinkedHashMap<Int, String>(dataSet.size).apply {
            var index = 0
            dataSet.forEach {
                put(index++, it.key)
            }
        }

    var dataSetCopy =
        ArrayMap<String, MutableCollection<PreferenceData>>(dataSet.size).apply {
            dataSet.forEach {
                put(it.key, mutableListOf())
            }
        }

    var groupsExpanded = SparseBooleanArray(dataSet.size).apply {
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
            newList = FlattenMap.addNullsToList(previousList, groupPosition, visibleItems)
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
        //Log.e("before", "$dataSetCopy  $title")
        dataSetCopy[title]?.clear()
        //Log.e("after", "$dataSetCopy")
        numberOfVisibleItems -= itemsInGroup
        RecyclerViewUpdater<Int?>().apply {
            val dataSetToList = FlattenMap.convertMapToList(dataSetCopy)
            //Log.e("dat after", "$dataSetToList")
            newList = dataSetToList
            oldList = FlattenMap.addNullsToList(dataSetToList, position, itemsInGroup)
            //Log.e("da befre", "$oldList")
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

    var groupToCheckedId = buildMutableMap<Int, Int>()

    var mGroupToEnabledID: MutableMap<Int, SparseBooleanArray> = buildMutableMap(3) {
        //initially they're all are enabled
        put(1, SparseBooleanArray(2).apply { append(4, true); append(8, true) })
        put(2, SparseBooleanArray(2).apply { append(5, true); append(9, true) })
        put(3, SparseBooleanArray(2).apply { append(6, true); append(10, true) })
    }

    //to make sure recently added is'nt removed
    override fun onChildClicked(position: Int, radioId: Int, buttonView: MyRadioButton) {
        val groupPosition = FlattenMap.getChildData(dataSetCopy, position).groupNumber
        val previousButton = groupToCheckedButton[groupPosition]
        if (previousButton.isNotNull()) {
            //Log.e("inn", "called")
            previousButton.isChecked = false
        }
        //Log.e("is checked", "${buttonView.isChecked}")
        groupToCheckedButton[groupPosition] = buttonView.apply { isChecked = true }
        groupToCheckedId[groupPosition] = buttonView.myId
        val index = if (groupPosition == 1) 1 else 0
        resetWhenDefault(radioId, position, index, groupPosition)
            ?: return //means it is default selected
        //to make sure only one of each is checked at a time and disable the other
        val viewsToEdit = addToMap(radioId) ?: return //we have no business if it's null
        val positionToDisable =
            if (groupPosition == 1) position + 5 else position - 5
        //Log.e("viewsToEdit", "$viewsToEdit")
        val otherGroupsMap = mGroupToEnabledID.map {
            val value = it.value
            value.keyAt(index) to value.valueAt(index)
        }.toMap()
        var previousIdToReEnable: Int? = null
        for ((i, isEnabled) in otherGroupsMap) {
            if (i == viewsToEdit.viewToDisable) continue
            if (!isEnabled)
                previousIdToReEnable = i
        }
        //Log.e("map", "gr1 $otherGroupsMap  $previousIdToReEnable  id $radioId $position ")
        previousIdToReEnable?.let {
            //Log.e("prev", "${mGroupToEnabledID[getIndex(it)]} $positionToDisable  ${viewsToEdit.viewToDisable - it}")
            mGroupToEnabledID[getIndex(it)]!!.put(it, true)
        }
        /**
         * first position is [position]
         * If the id to be disabled is opened, [FlattenMap.getType] would return [FlattenMap.CHILD]
         * if it isn't opened we move
         * */
        if (FlattenMap.getType(dataSetCopy, positionToDisable) == FlattenMap.CHILD) {
            //it could be a child but not for grouping or decimal separator
            val positionToDisableId =
                FlattenMap.getChildData(dataSetCopy, positionToDisable).radioId
            val shouldChange =
                //the id should be for the other group
                if (groupPosition == 1)
                    positionToDisableId in 8..10
                else positionToDisableId in 4..6
            /*Log.e(
                "wrong",
                "$position " +
                        " ${FlattenMap.getChildData(
                            dataSetCopy,
                            positionToDisable
                        )}  $shouldChange" +
                        " $positionToDisableId  r $groupPosition"
            )*/
            if (shouldChange) {
                notifyItemChanged(positionToDisable)
                //since the position to disable is in the same group as previous id to re enable
                // I can put the code to update it here
                previousIdToReEnable?.let {
                    val difference = viewsToEdit.viewToDisable - it
                    val adapterPositionForIt = positionToDisable - difference
                    notifyItemChanged(adapterPositionForIt)
                }
            }
        }
    }

    private fun resetWhenDefault(
        radioId: Int,
        position: Int,
        index: Int,
        groupPosition: Int
    ): Unit? {
        when (radioId) {
            3, 7 -> {
                //handle case for the default
                var previousIdToReEnable: Int? = null
                val otherGroupsMap = mGroupToEnabledID.map {
                    val value = it.value
                    value.keyAt(index) to value.valueAt(index)
                }.toMap()
                //Log.e("mp", "$mGroupToEnabledID index $index")
                //Log.e("other", "$otherGroupsMap")
                for ((i, isEnabled) in otherGroupsMap) {
                    if (!isEnabled) {
                        previousIdToReEnable = i
                        break // since only one can be false at a time
                    }
                }
                //should'nt be null though
                previousIdToReEnable?.let {
                    val adapterPosition =
                        if (groupPosition == 1)
                            position + (it - radioId) + 1
                        else position - (radioId - it) - 1
                    mGroupToEnabledID[getIndex(it)]!!.put(it, true)
                    if (FlattenMap.getType(dataSetCopy, adapterPosition) == FlattenMap.CHILD) {
                        //update if visible
                        //Log.e("prev", "$previousIdToReEnable  $radioId  $position sum $adapterPosition ")
                        //it could be a child but not for grouping or decimal separator
                        val positionToDisableId =
                            FlattenMap.getChildData(dataSetCopy, adapterPosition).radioId
                        val shouldChange =
                            //the id should be for the other group
                            if (groupPosition == 1)
                                positionToDisableId in 8..10
                            else positionToDisableId in 4..6
                        if (shouldChange) notifyItemChanged(adapterPosition)
                    }
                }
                return null //no need to go to the next line and return
            }
        }
        return Unit
    }

    private fun getIndex(int: Int): Int {
        return when (int) {
            4, 8 -> 1
            5, 9 -> 2
            6, 10 -> 3
            else -> TODO()
        }
    }

    /**Updates the map and return the positions to be updated*/
    private fun addToMap(radioId: Int): ViewsToEdit? {
        return when (radioId) {
            4, 8 -> {
                val idToDisable: Int
                val idClicked = if (radioId == 4) {
                    idToDisable = 8
                    4
                } else {
                    idToDisable = 4
                    8
                }
                enableAndDisable(1, idClicked, idToDisable)
                ViewsToEdit(idClicked, idToDisable)
            }
            5, 9 -> {
                val idToDisable: Int
                val idClicked = if (radioId == 5) {
                    idToDisable = 9
                    5
                } else {
                    idToDisable = 5
                    9
                }
                enableAndDisable(2, idClicked, idToDisable)
                ViewsToEdit(idClicked, idToDisable)
            }
            6, 10 -> {
                val idToDisable: Int
                val idClicked = if (radioId == 6) {
                    idToDisable = 10
                    6
                } else {
                    idToDisable = 6
                    10
                }
                enableAndDisable(3, idClicked, idToDisable)
                ViewsToEdit(idClicked, idToDisable)
            }
            else -> null
        }
    }

    private fun enableAndDisable(mGroup: Int, idToEnable: Int, idToDisable: Int) {
        mGroupToEnabledID[mGroup]!!.apply {
            put(idToEnable, true)
            put(idToDisable, false)
        }
    }

    private data class ViewsToEdit(val viewClicked: Int, val viewToDisable: Int)

    private fun enableRadioButton(radioButton: MyRadioButton) {
        radioButton.isEnabled = true
        radioButton.buttonTintList = ColorStateList.valueOf(color!!)
    }

    private fun disableRadioButton(radioButton: MyRadioButton) {
        radioButton.isEnabled = false
        radioButton.buttonTintList = ColorStateList.valueOf(Color.DKGRAY)
    }

    private val groupToCheckedButton = LinkedHashMap<Int, MyRadioButton>()

    var sliderValue = 6f
    override fun onTrackChanged(value: Float) {
        sliderValue = value
    }
}