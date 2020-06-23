package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R

class Inductance(override val context: Context) : RecyclerDataAbstractClass() {

    private val _henry = getString(R.string.henry)
    private val henry = _henry.toLowerCase(locale)
    private val henryUnit = getString(R.string.henry_unit)
    private val abhenry = getString(R.string.abhenry)
    private val abhenryUnit = getString(R.string.abhenry_unit)

    override fun getList() = buildRecyclerList(7) {
        add(_henry, henryUnit)
        add(kilo + henry, kiloSymbol + henryUnit)
        add(centi + henry, centiSymbol + henryUnit)
        add(milli + henry, milliSymbol + henryUnit)
        add(micro + henry, microSymbol + henryUnit)
        add(nano + henry, nanoSymbol + henryUnit)
        add(abhenry, abhenryUnit)
    }
}