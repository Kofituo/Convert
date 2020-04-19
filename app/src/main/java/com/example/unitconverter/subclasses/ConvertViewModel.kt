package com.example.unitconverter.subclasses

import android.util.Log
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

    var dataSet = mutableListOf<RecyclerDataClass>()
        set(value) {
            if (field.isEmpty()) {
                field = value
            }
            Log.e("value", "$value  size${value.size}")
        }
    var whichButton = -1

    var selectedFavourites: MutableMap<Int, FavouritesData>? = null

    var favouritesProgress = 0f
        get() {
            val result = field
            field = 1f
            return result
        }
}