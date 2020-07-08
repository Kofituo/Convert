package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils.appendWithSpace
import java.text.NumberFormat

class FuelEconomy(override val context: Context) : RecyclerDataAbstractClass() {

    private val metreU = getString(R.string.metre)
    private val metre = metreU.toLowerCase(locale)
    private val metreUnit = getString(R.string.metre_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val litre = getString(R.string.litre).toLowerCase(locale)
    private val litreUnit = getString(R.string.litre_unit)
    private val miles = getString(R.string.mile)
    private val mileUnit = getString(R.string.mile_unit)
    private val foot = getString(R.string.foot)
    private val feetUnit = getString(R.string.foot_unit)
    private val inch = getString(R.string.inch)
    private val inchUnit = getString(R.string.inch_unit)
    private val usGallon = getString(R.string.gallon_us)
    private val ukGallon = getString(R.string.uk_gallon_unit)
    private val gallonUnit = getString(R.string.gallon_unit)

    override fun getList() = buildRecyclerList(21) {
        add(
            buildString {
                appendWithSpace(metreU)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        putEntry {
            quantity = buildString {
                append(kilo)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(litre)
            }
            unit = buildString {
                append(kiloSymbol)
                append(perUnit)
                append(litreUnit)
            }
        }
        add(
            buildString {
                append(hecto)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(hectoSymbol)
                append(metreUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                append(deca)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(decaSymbol)
                append(metreUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                append(deci)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(deciSymbol)
                append(metreUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(miles)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(mileUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(foot)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(feetUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(inch)
                appendWithSpace(per)
                append(litre)
            }, buildString {
                append(inchUnit)
                append(perUnit)
                append(litreUnit)
            }
        )
        add(
            buildString {
                appendWithSpace(miles)
                appendWithSpace(per)
                append(usGallon)
            }, getString(R.string.mpg)
        )
        add(
            buildString {
                appendWithSpace(miles)
                appendWithSpace(per)
                append(ukGallon)
            }, getString(R.string.mpg)
        )
        add(
            buildString {
                appendWithSpace(metreU)
                appendWithSpace(per)
                append(usGallon)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(kilo)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(usGallon)
            }, buildString {
                append(kiloSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(hecto)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(usGallon)
            }, buildString {
                append(hectoSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(deca)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(usGallon)
            }, buildString {
                append(decaSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(deci)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(usGallon)
            }, buildString {
                append(deciSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        ////////////////////////
        add(
            buildString {
                appendWithSpace(metreU)
                appendWithSpace(per)
                append(ukGallon)
            }, buildString {
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(kilo)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(ukGallon)
            }, buildString {
                append(kiloSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(hecto)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(ukGallon)
            }, buildString {
                append(hectoSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(deca)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(ukGallon)
            }, buildString {
                append(decaSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        add(
            buildString {
                append(deci)
                appendWithSpace(metre)
                appendWithSpace(per)
                append(ukGallon)
            }, buildString {
                append(deciSymbol)
                append(metreUnit)
                append(perUnit)
                append(gallonUnit)
            }
        )
        val hundred = NumberFormat.getNumberInstance().format(100)
        add(
            buildString {
                appendWithSpace(getString(R.string.litre))
                appendWithSpace(per)
                appendWithSpace(hundred)
                append(kiloSymbol)
                append(metreUnit)
            }, buildString {
                append(litreUnit)
                append(perUnit)
                append(hundred)
                append(kiloSymbol)
                append(metreUnit)
            }
        )
    }
}