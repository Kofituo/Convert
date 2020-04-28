package com.example.unitconverter.subclasses

import androidx.lifecycle.ViewModel
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.miscellaneous.ResetAfterNGets
import com.example.unitconverter.miscellaneous.ResetAfterNGets.Companion.resetAfterGet

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    val motionProgress by resetAfterGet(0f,1f)

    lateinit var dataSet: MutableList<RecyclerDataClass>

    val dataSetIsInit
        get() = ::dataSet.isInitialized

    var whichButton = -1

    var selectedFavourites: MutableMap<Int, FavouritesData>? = null

    val favouritesProgress by resetAfterGet(0f,1f)
}