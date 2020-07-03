package com.example.unitconverter.subclasses

import android.util.SparseBooleanArray
import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.miscellaneous.ResetAfterNGets.Companion.resetAfterGet

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    lateinit var dataSet: MutableList<RecyclerDataClass>

    val dataSetIsInit
        get() = ::dataSet.isInitialized

    var whichButton = -1

    /**
     * position selected to data it holds
     * */
    var selectedFavourites: MutableMap<Int, FavouritesData>? = null

    val favouritesProgress by resetAfterGet(0f, 1f)

    var preferenceMap: Map<String, Collection<PreferenceData>>? = null

    var dataSetCopy: ArrayMap<String, MutableCollection<PreferenceData>>? = null

    var numberOfItems: Int? = null

    var groupsExpanded: SparseBooleanArray? = null

    var visibleItemsPerGroup: MutableMap<Int, Int>? = null

    var sliderValue: Float? = null

    var groupToCheckedId: Map<Int, Int>? = null

    var initialSliderValue: Float? = null

    var initialMap: Map<Int, Int>? = null

    var mGroupToEnabledID: MutableMap<Int, SparseBooleanArray>? = null

    var defaultIdToIsChecked: MutableMap<Int, Boolean>? = null

    fun clearForPreferences() {
        dataSetCopy = null
        numberOfItems = null
        initialMap = null
        initialSliderValue = null
        groupsExpanded = null
        visibleItemsPerGroup = null
        sliderValue = null
        groupToCheckedId = null
        mGroupToEnabledID = null
        defaultIdToIsChecked = null
    }

    var searchIsExpanded = false
}