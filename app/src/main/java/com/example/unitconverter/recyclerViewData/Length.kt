package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import java.util.*

class Length(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() = buildRecyclerList(31) {
        add(R.string.metre, R.string.metre_unit)
        addAll(
            massPrefixes(
                getString(R.string.metre).toLowerCase(Locale.getDefault()),
                getString(R.string.metre_unit)
            )
        )
        add(R.string.foot, R.string.foot_unit)
        add(R.string.inch, R.string.inch_unit)
        add(R.string.mile, R.string.mile_unit)
        add(R.string.yard, R.string.yard_unit)
        add(R.string.nautical_mile, R.string.nautical_mile_unit)
        add(R.string.nautical_league, R.string.nautical_league_unit)
        add(R.string.fathom, R.string.fathom_unit)
        add(R.string.rod, R.string.rod_unit)
        add(R.string.thou, R.string.thou_unit)
        add(R.string.chain, R.string.chain_unit)
        add(R.string.furlong, R.string.furlong_unit)
        add(R.string.angstrom, R.string.angstrom_unit)
        add(R.string.planck_length, R.string.planck_length_unit)
        add(R.string.light_year, R.string.light_year_unit)
    }
}