package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Time(override val context: Context) : RecyclerDataInterface {
    override var start = 0

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(R.string.seconds, R.string.seconds_unit)
            addAll(
                massPrefixes(
                    getString(R.string.seconds).toLowerCase(locale),
                    getString(R.string.seconds_unit)
                )
            )
        }
    }

}