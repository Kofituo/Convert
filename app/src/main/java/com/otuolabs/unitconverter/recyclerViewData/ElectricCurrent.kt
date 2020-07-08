package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R

class ElectricCurrent(override val context: Context) : RecyclerDataAbstractClass() {

    private val _amp = getString(R.string.amp)
    private val amp = _amp.toLowerCase(locale)
    private val ampUnit = getString(R.string.amp_unit)
    private val abAmp = getString(R.string.abAmp)
    private val abAmpUnit = getString(R.string.abAmp_unit)

    override fun getList() = buildRecyclerList(7) {
        add(_amp, ampUnit)
        add(kilo + amp, kiloSymbol + ampUnit)
        add(centi + amp, centiSymbol + ampUnit)
        add(milli + amp, milliSymbol + ampUnit)
        add(micro + amp, microSymbol + ampUnit)
        add(nano + amp, nanoSymbol + ampUnit)
        add(abAmp, abAmpUnit)
    }
}