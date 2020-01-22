package com.example.unitconverter.subclasses

import androidx.lifecycle.ViewModel

class ConvertViewModel : ViewModel() {
    var randomInt = 0
    var motionProgress = 0f
    var dataSet = mutableListOf<RecyclerDataClass>()
        set(value) {
            if (field.isEmpty()) {
                field = value
            }
        }
}