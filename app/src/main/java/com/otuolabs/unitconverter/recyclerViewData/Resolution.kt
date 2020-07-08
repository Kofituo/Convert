package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.add
import com.otuolabs.unitconverter.miscellaneous.addWithSpace
import com.otuolabs.unitconverter.miscellaneous.appendString

class Resolution(override val context: Context) : RecyclerDataAbstractClass() {

    private val pixel = getString(R.string.pixel)
    private val pixelUnit = getString(R.string.pPer)
    private val per = getString(R.string.per)
    private val metre = getString(R.string.metre).toLC()
    private val metreUnit = getString(R.string.metre_unit)

    override fun getList() =
        buildRecyclerList(4) {
            putEntry {
                quantity = appendString {
                    addWithSpace { pixel }
                    addWithSpace { per }
                    add { getString(R.string.inch) }
                }
                unit = appendString {
                    add { pixelUnit }
                    add { pixelUnit }
                    add { getString(R.string.inch_unit) }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pixel }
                    addWithSpace { per }
                    add { metre }
                }
                unit = appendString {
                    add { pixelUnit }
                    add { pixelUnit }
                    add { metreUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pixel }
                    addWithSpace { per }
                    add { centi }
                    add { metre }
                }
                unit = appendString {
                    add { pixelUnit }
                    add { pixelUnit }
                    add { centiSymbol }
                    add { metreUnit }
                }
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { pixel }
                    addWithSpace { per }
                    add { milli }
                    add { metre }
                }
                unit = appendString {
                    add { pixelUnit }
                    add { pixelUnit }
                    add { milliSymbol }
                    add { metreUnit }
                }
            }
        }
}