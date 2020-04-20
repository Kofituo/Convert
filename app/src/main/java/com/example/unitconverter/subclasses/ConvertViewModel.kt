package com.example.unitconverter.subclasses

import androidx.lifecycle.ViewModel
import com.example.unitconverter.RecyclerDataClass

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    var motionProgress = 0f
        get() {
            val result = field
            field = 1f
            return result
        }

    lateinit var dataSet: MutableList<RecyclerDataClass>

    val dataSetIsInit get() = ::dataSet.isInitialized

    var whichButton = -1

    var selectedFavourites: MutableMap<Int, FavouritesData>? = null

    var favouritesProgress = 0f
        get() {
            val result = field
            field = 1f
            return result
        }
}