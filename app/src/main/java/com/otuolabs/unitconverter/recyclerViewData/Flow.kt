package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.add
import com.otuolabs.unitconverter.miscellaneous.addWithSpace
import com.otuolabs.unitconverter.miscellaneous.appendString

class Flow(override val context: Context) : RecyclerDataAbstractClass() {

    private val metre = getString(R.string.metre).toLC()
    private val metreUnit = getString(R.string.metre_unit)
    private val cube = getString(R.string.cubic)
    private val cubeUnit = getString(R.string.cubic_unit)
    private val second = getString(R.string.seconds).toLC()
    private val secondUnit = getString(R.string.seconds_unit)
    private val minute = getString(R.string.minute).toLC()
    private val minuteUnit = getString(R.string.minute_unit)
    private val hour = getString(R.string.hour).toLC()
    private val hourUnit = getString(R.string.hour_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val gal = getString(R.string.us_gallon)
    private val impGal = getString(R.string.uk_gallon)
    private val galUnit = getString(R.string.gall_unit)
    private val foot = getString(R.string.foot).toLC()
    private val footUnit = getString(R.string.foot_unit)
    private val inch = getString(R.string.inch).toLC()
    private val inchUnit = getString(R.string.inch_unit)
    private val litre = getString(R.string.litre)
    private val litreUnit = getString(R.string.litre_unit)

    override val centi: String
        get() = super.centi.toLC()
    override val milli: String
        get() = super.milli.toLC()

    override fun getList() =
        buildRecyclerList(27) {
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { centi }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { centiSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { centi }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { centiSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { centi }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { centiSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { milli }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { milli }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    add { milli }
                    addWithSpace { metre }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { metreUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { litre }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { litreUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { litre }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { litreUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { litre }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { litreUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            val lcLitre = litre.toLC()
            putEntry {
                quantity = appendString {
                    add { super.milli }
                    addWithSpace { lcLitre }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { litreUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    add { super.milli }
                    addWithSpace { lcLitre }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { litreUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    add { super.milli }
                    addWithSpace { lcLitre }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { litreUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { foot }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { footUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { foot }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { footUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { foot }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { footUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { inch }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { inchUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { inch }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { inchUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { cube }
                    addWithSpace { inch }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { inchUnit }
                    add { cubeUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { gal }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { gal }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { gal }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { impGal }
                    addWithSpace { per }
                    add { second }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { secondUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { impGal }
                    addWithSpace { per }
                    add { minute }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { minuteUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { impGal }
                    addWithSpace { per }
                    add { hour }
                }
                unit = appendString {
                    add { galUnit }
                    add { perUnit }
                    add { hourUnit }
                }
            }
        }
}