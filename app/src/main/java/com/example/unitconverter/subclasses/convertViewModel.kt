package com.example.unitconverter.subclasses

import android.util.Log
import androidx.lifecycle.ViewModel

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    var motionProgress = 0f
    var dataSet = mutableListOf<RecyclerDataClass>()
        set(value) {
            Log.e("called", "called")
            if (field.isEmpty()) {
                Log.e("1", "1")
                field = value
            }
        }

    var topPosition = -1
}