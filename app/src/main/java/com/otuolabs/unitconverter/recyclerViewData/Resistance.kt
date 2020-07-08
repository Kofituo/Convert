package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R

class Resistance(override val context: Context) : RecyclerDataAbstractClass() {

    private val _ohm = getString(R.string.ohm)
    private val ohmUnit = getString(R.string.ohm_unit)
    private val ohm = _ohm.toLowerCase(locale)

    override fun getList() = buildRecyclerList(19) {
        putEntry {
            quantity = _ohm
            unit = ohmUnit
        }
        this putAll massPrefixes(ohm, ohmUnit)
        putEntry {
            quantity = getString(R.string.abohms)
            unit = getString(R.string.abohm_unit)
        }
        putEntry {
            quantity = getString(R.string.statohm)
            unit = getString(R.string.statohm_unit)
        }
    }
}