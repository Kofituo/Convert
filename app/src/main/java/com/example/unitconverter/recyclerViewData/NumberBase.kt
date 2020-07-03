package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.add
import com.example.unitconverter.miscellaneous.addWithSpace
import com.example.unitconverter.miscellaneous.appendString
import java.text.NumberFormat

class NumberBase(override val context: Context) : RecyclerDataAbstractClass() {

    private val decimalFormat: NumberFormat = NumberFormat.getInstance(locale)
    private val base = getString(R.string.base)

    override fun getList() =
        buildRecyclerList(30) {
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(2) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(3) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(4) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(5) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(6) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(7) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(8) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(9) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(10) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(11) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(12) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(13) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(14) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(15) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(16) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(32) }
                }
                unit = ""
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(36) }
                }
                unit = ""
            }
        }
}