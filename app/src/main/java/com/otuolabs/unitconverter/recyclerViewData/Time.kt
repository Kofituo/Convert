package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R

class Time(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() = buildRecyclerList(31) {
        add(R.string.seconds, R.string.seconds_unit)
        addAll(
            massPrefixes(
                getString(R.string.seconds).toLowerCase(locale),
                getString(R.string.seconds_unit)
            )
        )
        add(R.string.minute, R.string.minute_unit)
        add(R.string.hour, R.string.hour_unit)
        add(R.string.day, R.string.day)
        add(R.string.week, R.string.week)
        add(R.string.fortnight, R.string.fortnight_unit)
        add(R.string.month, R.string.month_unit)
        add(R.string.c_year, R.string.year_unit)
        add(R.string.year_exact, R.string.year_unit)
        add(R.string.leap_year, R.string.leap_year_unit)
        add(R.string.decade, R.string.decade_unit)
        add(R.string.century, R.string.century_unit)
        add(R.string.millennium, R.string.millennium_unit)
        add(R.string.planck_time, R.string.planck_time_unit)
        add(R.string.svedberg, R.string.svedberg_unit)
    }

}