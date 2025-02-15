package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.RecyclerDataClass
import java.util.*

class Area(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() =
        buildRecyclerList(19) {

            squareAdd(R.string.metre, R.string.metre_unit)
            val quantity = getString(R.string.metre).toLowerCase(locale)
            val unit = getString(R.string.metre_unit)
            add(
                square + " " + kilo.toLowerCase(locale) + quantity,
                kiloSymbol + unit + squareSymbol
            )
            add(
                square + " " + hecto.toLowerCase(locale) + quantity,
                hectoSymbol + unit + squareSymbol
            )
            add(
                square + " " + deca.toLowerCase(locale) + quantity,
                decaSymbol + unit + squareSymbol
            )
            add(
                square + " " + deci.toLowerCase(locale) + quantity,
                deciSymbol + unit + squareSymbol
            )
            add(
                square + " " + centi.toLowerCase(locale) + quantity,
                centiSymbol + unit + squareSymbol
            )
            add(
                square + " " + milli.toLowerCase(locale) + quantity,
                milliSymbol + unit + squareSymbol
            )
            add(
                square + " " + micro.toLowerCase(locale) + quantity,
                microSymbol + unit + squareSymbol
            )
            squareAdd(R.string.foot, R.string.foot_unit)
            squareAdd(R.string.inch, R.string.inch_unit)
            squareAdd(R.string.yard, R.string.yard_unit)
            squareAdd(R.string.mile, R.string.mile_unit)
            squareAdd(R.string.nautical_mile, R.string.nautical_mile_unit)
            squareAdd(R.string.nautical_league, R.string.nautical_league_unit)
            squareAdd(R.string.chain, R.string.chain_unit)
            add(R.string.acre, R.string.acre_unit)
            add(R.string.hectare, R.string.hectare_unit)
            add(R.string.are, R.string.are_unit)
            add(R.string.atm_barn, R.string.atm_barn_unit)
        }

    private fun MutableList<RecyclerDataClass>.squareAdd(quantity: Int, unit: Int) =
        add(
            square + " " + getString(quantity).toLowerCase(Locale.getDefault()),
            getString(unit) + squareSymbol
        )
}