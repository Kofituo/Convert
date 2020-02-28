package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.Utils.appendWithSpace

class Speed(override val context: Context) : RecyclerDataAbstractClass() {

    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val seconds = getString(R.string.seconds).toLowerCase(locale)
    private val secondsUnit = getString(R.string.seconds_unit)
    private val hour = getString(R.string.hour).toLowerCase(locale)
    private val hourUnit = getString(R.string.hour_unit)
    private val minute = getString(R.string.minute).toLowerCase(locale)
    private val minuteUnit = getString(R.string.minute_unit)
    private val metre = getString(R.string.metre).toLowerCase(locale)
    private val metreUnit = getString(R.string.metre_unit)

    override fun getList() = buildRecyclerList {

        add(
            buildString {
                appendWithSpace(getString(R.string.metre))
                appendWithSpace(per)
                append(seconds)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(secondsUnit)
            }
        )
        addAll(
            massPrefixes(buildString {
                appendWithSpace(metre)
                appendWithSpace(per)
                append(seconds)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(secondsUnit)
            })
        )
        add(
            buildString {
                append(kilo)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(hour)
            }, buildString {
                append(kiloSymbol)
                append(metreUnit)
                append(perUnit)
                append(hourUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.mile))
                appendWithSpace(per)
                append(hour)
            }, buildString {
                append(getString(R.string.mile_unit))
                append(perUnit)
                append(hourUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.mile))
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(getString(R.string.mile_unit))
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.mile))
                appendWithSpace(per)
                append(seconds)
            }, buildString {
                append(getString(R.string.mile_unit))
                append(perUnit)
                append(secondsUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.metre))
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(kilo)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(kiloSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(hecto)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(hectoSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(deca)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(decaSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(deci)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(deciSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(centi)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(centiSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                append(milli)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(minute)
            }, buildString {
                append(milliSymbol)
                append(metreUnit)
                append(perUnit)
                append(minuteUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.foot))
                appendWithSpace(per)
                append(seconds)
            }, buildString {
                append(getString(R.string.foot_unit))
                append(perUnit)
                append(secondsUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(getString(R.string.knot))
                appendWithSpace(per)
                append(seconds)
            }, buildString {
                append(getString(R.string.knot).toLowerCase(locale))
                append(perUnit)
                append(secondsUnit)
            }
        )
        add(R.string.speed_of_light, R.string.speed_of_light_unit)
    }
}