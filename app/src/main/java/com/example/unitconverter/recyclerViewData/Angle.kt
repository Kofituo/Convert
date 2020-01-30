package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Angle(override val context: Context) : RecyclerDataInterface {

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(R.string.degrees, R.string.degrees_unit)
            add(R.string.radians, R.string.radians_unit)
            add(R.string.grad, R.string.grad_unit)
            add(R.string.revolution, R.string.revolution_unit)
            add(R.string.arc_minute, R.string.arc_minute_unit)
            add(R.string.arc_second, R.string.arc_second_unit)
        }
    }

    override var start = 0
}