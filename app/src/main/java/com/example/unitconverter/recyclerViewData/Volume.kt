package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Volume(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() = buildRecyclerList(28) {
        cubicAdd(R.string.metre, R.string.metre_unit)
        val quantity = getString(R.string.metre).toLowerCase(locale)
        val unit = getString(R.string.metre_unit)
        add(
            cubic + " " + kilo.toLowerCase(locale) + quantity,
            kiloSymbol + unit + cubicUnit
        )
        add(
            cubic + " " + hecto.toLowerCase(locale) + quantity,
            hectoSymbol + unit + cubicUnit
        )
        add(
            cubic + " " + deca.toLowerCase(locale) + quantity,
            decaSymbol + unit + cubicUnit
        )
        add(
            cubic + " " + deci.toLowerCase(locale) + quantity,
            deciSymbol + unit + cubicUnit
        )
        add(
            cubic + " " + centi.toLowerCase(locale) + quantity,
            centiSymbol + unit + cubicUnit + " " + getString(R.string.cubic_centimetre)
        )
        add(
            cubic + " " + milli.toLowerCase(locale) + quantity,
            milliSymbol + unit + cubicUnit
        )
        add(
            cubic + " " + micro.toLowerCase(locale) + quantity,
            microSymbol + unit + cubicUnit
        )
        val litre = getString(R.string.litre).toLowerCase(locale)
        val litreUnit = getString(R.string.litre_unit)
        add(R.string.litre, R.string.litre_unit)
        add(kilo + litre, kiloSymbol + litreUnit)
        add(hecto + litre, hectoSymbol + litreUnit)
        add(deca + litre, decaSymbol + litreUnit)
        add(deci + litre, deciSymbol + litreUnit)
        add(centi + litre, centiSymbol + litreUnit)
        add(milli + litre, milliSymbol + litreUnit)
        add(micro + litre, microSymbol + litreUnit)
        cubicAdd(R.string.inch, R.string.inch_unit)
        cubicAdd(R.string.foot, R.string.foot_unit)
        cubicAdd(R.string.yard, R.string.yard_unit)
        add(R.string.us_gallon, R.string.us_gallon_unit)
        add(R.string.uk_gallon, R.string.uk_gallon_unit)
        add(R.string.us_pint, R.string.us_pint_unit)
        add(R.string.imperial_pint, R.string.imperial_pint_unit)
        add(R.string.barrel, R.string.barrel_unit)
        add(R.string.us_fluid_ounce, R.string.us_fluid_ounce_unit)
        add(R.string.uk_fluid_ounce, R.string.uk_fluid_ounce_unit)
        add(R.string.quart, R.string.quart_unit)
        add(R.string.uk_quart, R.string.uk_quart_unit)
    }

    private fun MutableList<RecyclerDataClass>.cubicAdd(quantity: Int, unit: Int) =
        add("$cubic ${getString(quantity).toLowerCase(locale)}", getString(unit) + cubicUnit)

    // gets assigned once
    private val cubic = getString(R.string.cubic)
    private val cubicUnit = getString(R.string.cubic_unit)
}