package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.Utils.appendWithSpace

class Luminance(override val context: Context) : RecyclerDataAbstractClass() {

    private val _candela = getString(R.string.candela)
    private val candela = _candela.toLowerCase(locale)
    private val candelaUnit = getString(R.string.candela_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val metre = getString(R.string.metre).toLowerCase(locale)
    private val metreUnit = getString(R.string.metre_unit)
    private val _square = square.toLowerCase(locale)

    override fun getList() = buildRecyclerList(20) {
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                append(micro)
                appendWithSpace(candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(microSymbol)
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                append(milli)
                appendWithSpace(candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(milliSymbol)
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                append(kilo)
                appendWithSpace(candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(kiloSymbol)
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                append(mega)
                appendWithSpace(candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(megaSymbol)
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                append(giga)
                appendWithSpace(candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(metre)
            }, buildString {
                append(gigaSymbol)
                append(candelaUnit)
                append(perUnit)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        nit {
            add(this, this.toLowerCase(locale))
        }
        add(getString(R.string.stilb), getString(R.string.stilb_unit))
        add(getString(R.string.apostilb), getString(R.string.apostilb_unit))
        add(getString(R.string.lambert), getString(R.string.lambert_unit))
        add(getString(R.string.foot_lambert), getString(R.string.foot_lambert_unit))
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(getString(R.string.foot).toLowerCase(locale))
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(getString(R.string.foot_unit))
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(getString(R.string.inch).toLowerCase(locale))
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(getString(R.string.inch_unit))
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(micro.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(microSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(milli.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(milliSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(centi.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(centiSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(deci.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(deciSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(deca.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(decaSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(hecto.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(hectoSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
        add(
            buildString {
                appendWithSpace(_candela)
                appendWithSpace(per)
                appendWithSpace(_square)
                append(kilo.toLowerCase(locale))
                append(metre)
            }, buildString {
                append(candelaUnit)
                append(perUnit)
                append(kiloSymbol)
                append(metreUnit)
                append(squareSymbol)
            }
        )
    }

    //just a little spicing
    private fun nit(action: String.() -> Unit) = getString(R.string.nit).apply(action)
}