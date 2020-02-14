package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Speed(override val context: Context) : RecyclerDataInterface {
    override var start = 0

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
            add(
                getString(R.string.metre) + " "
                        + getString(R.string.per) + " "
                        + getString(R.string.seconds).toLowerCase(locale),
                getString(R.string.metre_unit)
                        + getString(R.string.per_unit)
                        + getString(R.string.seconds_unit)
            )
            addAll(
                massPrefixes(
                    getString(R.string.metre).toLowerCase(locale) + " "
                            + getString(R.string.per) + " "
                            + getString(R.string.seconds).toLowerCase(locale),
                    getString(R.string.metre_unit)
                            + getString(R.string.per_unit)
                            + getString(R.string.seconds_unit)
                )
            )
            add(
                kilo + getString(R.string.metre).toLowerCase(locale) + " "
                        + getString(R.string.per) + " "
                        + getString(R.string.hour).toLowerCase(locale),
                kiloSymbol + getString(R.string.metre_unit)
                        + getString(R.string.per_unit)
                        + getString(R.string.hour_unit)
            )
        }
    }
}