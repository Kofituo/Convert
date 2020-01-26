package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.RecyclerDataClass

class Area(override val context: Context) : RecyclerDataInterface {

    override var start = 0

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {

        }
    }
}