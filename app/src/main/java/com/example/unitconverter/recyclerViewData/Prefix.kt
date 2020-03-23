package com.example.unitconverter.recyclerViewData

import android.content.Context

class Prefix(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() =
        buildRecyclerList(20) {
            add(yotta, yottaSymbol)
            add(zetta, zettaSymbol)
            addAll(massPrefixes())
            add(zepto, zeptoSymbol)
            add(yocto, yoctoSymbol)
        }
}