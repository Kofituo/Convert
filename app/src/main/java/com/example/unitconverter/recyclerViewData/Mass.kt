package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Mass(override val context: Context) : RecyclerDataInterface() {

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(R.string.gram, R.string.gram_unit)
            addAll(
                massPrefixes(
                    getString(R.string.gram).toLowerCase(locale),
                    getString(R.string.gram_unit)
                )
            )
            add(R.string.pound, R.string.pound_unit)
            add(R.string.ounce, R.string.ounce_unit)
            add(R.string.metric_ton, R.string.metricTonUnit)
            add(R.string.short_ton, R.string.short_ton_unit)
            add(R.string.long_ton, R.string.long_ton_unit)
            add(R.string.carat, R.string.carat_unit)
            add(R.string.grain, R.string.grain_unit)
            add(R.string.troy_pound, R.string.troy_poundUnit)
            add(R.string.troy_ounce, R.string.troyOunceUnit)
            add(R.string.pennyweight, R.string.pennyweightUnit)
            add(R.string.stone, R.string.stone_unit)
            add(R.string.slug_mass, R.string.slug_unit)
            add(R.string.atomicMassUnit, R.string.atomic_mass_unit_unit)
            add(R.string.planck_mass, R.string.planck_mass_unit)
            add(R.string.solar_mass, R.string.solar_mass_unit)
        }
    }
}