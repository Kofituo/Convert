package com.example.unitconverter.conversionClasses

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Temperature(override val context: Context) : RecyclerDataInterface {

    override var start = 0
    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(R.string.celsius, R.string.celsius_unit)
            add(R.string.fahrenheit, R.string.fahrenheit_unit)
            add(R.string.kelvin, R.string.kelvin_unit)
        }
    }
}