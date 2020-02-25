package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Pressure(override val context: Context) : RecyclerDataInterface {
    override var start = 0

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            val pascal = getString(R.string.pascal)
            val pascalUnit = getString(R.string.pascal_unit)
            val bar = getString(R.string.bar).toLowerCase(locale)
            val barUnit = getString(R.string.bar_unit)

            add(pascal, pascalUnit)

            addAll(massPrefixes(pascal.toLowerCase(locale), pascalUnit))
            val mercury = getString(R.string.mercury)
            val mercuryUnit = getString(R.string.mercury_unit)

            add(
                getString(R.string.inch) + " " + mercury,
                getString(R.string.inch_unit) + mercuryUnit
            )
            getString(R.string.metre).toLowerCase(locale).apply {
                getString(R.string.metre_unit).also {
                    add("$centi$this $mercury", centiSymbol + it + mercuryUnit)
                    add("$milli$this $mercury", milliSymbol + it + mercuryUnit)
                    add("$micro$this $mercury", microSymbol + it + mercuryUnit)
                }
            }
            add(R.string.bar, R.string.bar_unit)

            add(giga + bar, gigaSymbol + barUnit)
            add(mega + bar, megaSymbol + barUnit)
            add(kilo + bar, kiloSymbol + barUnit)
            add(hecto + bar, hectoSymbol + barUnit)
            add(deca + bar, decaSymbol + barUnit)
            add(deci + bar, deciSymbol + barUnit)
            add(centi + bar, centiSymbol + barUnit)
            add(milli + bar, milliSymbol + barUnit)
            add(
                R.string.grams_force_per_square_centimetre,
                R.string.grams_force_per_square_centimetre_unit
            )
            add(
                R.string.kilograms_force_per_square_centimetre,
                R.string.kilograms_force_per_square_centimetre_unit
            )
            add(
                getString(R.string.pound) + " "
                        + getString(R.string.per) + " "
                        + square.toLowerCase(locale) + " "
                        + getString(R.string.inch).toLowerCase(locale), getString(R.string.psi)
            )
            add(R.string.atm, R.string.atm_unit)
            add(R.string.torr, R.string.torr)
        }
    }
}