package com.example.unitconverter.conversionClasses

import android.content.Context
import com.example.unitconverter.RecyclerDataClass

class Prefix(override val context: Context) : RecyclerDataInterface {

    override var start = 0
    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(yotta, yottaSymbol)
            add(zetta, zettaSymbol)
            addAll(massPrefixes())
            add(zepto, zeptoSymbol)
            add(yocto, yoctoSymbol)
        }
    }
}