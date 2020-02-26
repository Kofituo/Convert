package com.example.unitconverter.subclasses

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.unitconverter.RecyclerDataClass

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    var motionProgress = 0f
    var dataSet = mutableListOf<RecyclerDataClass>()
        set(value) {
            if (field.isEmpty()) {
                field = value
            }
            Log.e("value", "$value  ${value.size}")
        }
    var whichButton = -1
}