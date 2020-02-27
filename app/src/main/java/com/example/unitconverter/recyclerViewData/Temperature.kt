package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Temperature(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(R.string.celsius, R.string.celsius_unit)
            add(R.string.fahrenheit, R.string.fahrenheit_unit)
            add(R.string.kelvin, R.string.kelvin_unit)
            add(R.string.newton_temperature, R.string.newton_temperature_unit)
            add(R.string.Delisle, R.string.delisle_unit)
            add(R.string.Rankine, R.string.rankine_unit)
            add(R.string.romer, R.string.rømer_unit)
            add(R.string.réaumur, R.string.réaumur_unit)
        }
    }
}