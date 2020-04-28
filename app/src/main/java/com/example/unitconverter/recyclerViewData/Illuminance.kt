package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.Utils.appendWithSpace

class Illuminance(override val context: Context) : RecyclerDataAbstractClass() {

    private val lumen = getString(R.string.lumen)
    private val lumenUnit = getString(R.string.lumen_unit)
    private val per = getString(R.string.per).toLowerCase(locale)
    private val perUnit = getString(R.string.per_unit)
    private val metre = getString(R.string.metre).toLowerCase(locale)
    private val _lux = getString(R.string.lux)
    private val lux = _lux.toLowerCase(locale)
    private val luxUnit = getString(R.string.lux_unit)
    private val metreUnit = getString(R.string.metre_unit)
    override fun getList() =
        buildRecyclerList(10) {
            putEntry {
                quantity = _lux
                unit = luxUnit
            }
            putEntry {
                quantity = "$kilo$lux"
                unit = "$kiloSymbol$luxUnit"
            }
            putEntry {
                quantity = "$micro$lux"
                unit = "$microSymbol$luxUnit"
            }
            putEntry {
                quantity = getString(R.string.phot)
                unit = getString(R.string.phot_unit)
            }
            putEntry {
                quantity = getString(R.string.nox)
                unit = getString(R.string.nox_unit)
            }
            putEntry {
                quantity = buildString {
                    appendWithSpace(lumen)
                    appendWithSpace(per)
                    appendWithSpace(square)
                    append(deca)
                    append(metre)
                }
                unit = buildString {
                    append(lumenUnit)
                    append(perUnit)
                    append(decaSymbol)
                    append(metreUnit)
                    append(squareSymbol)
                }
            }
            putEntry {
                quantity = buildString {
                    appendWithSpace(lumen)
                    appendWithSpace(per)
                    appendWithSpace(square)
                    append(deci)
                    append(metre)
                }
                unit = buildString {
                    append(lumenUnit)
                    append(perUnit)
                    append(deciSymbol)
                    append(metreUnit)
                    append(squareSymbol)
                }
            }
            putEntry {
                quantity = buildString {
                    appendWithSpace(lumen)
                    appendWithSpace(per)
                    appendWithSpace(square)
                    append(centi)
                    append(metre)
                }
                unit = buildString {
                    append(lumenUnit)
                    append(perUnit)
                    append(centiSymbol)
                    append(metreUnit)
                    append(squareSymbol)
                }
            }
            putEntry {
                quantity = buildString {
                    appendWithSpace(lumen)
                    appendWithSpace(per)
                    appendWithSpace(square)
                    append(milli)
                    append(metre)
                }
                unit = buildString {
                    append(lumenUnit)
                    append(perUnit)
                    append(milliSymbol)
                    append(metreUnit)
                    append(squareSymbol)
                }
            }
            putEntry {
                quantity = getString(R.string.candle)
                unit = getString(R.string.candle_unit)
            }
            putEntry {
                quantity = buildString {
                    appendWithSpace(lumen)
                    appendWithSpace(per)
                    appendWithSpace(square)
                    append(getString(R.string.inch))
                }
                unit = buildString {
                    append(lumenUnit)
                    append(perUnit)
                    append(getString(R.string.inch_unit))
                    append(squareSymbol)
                }
            }
        }
}