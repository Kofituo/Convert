package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.add
import com.otuolabs.unitconverter.miscellaneous.addWithSpace
import com.otuolabs.unitconverter.miscellaneous.appendString
import java.text.NumberFormat

class NumberBase(override val context: Context) : RecyclerDataAbstractClass() {

    private val decimalFormat: NumberFormat = NumberFormat.getInstance(locale)
    private val base = getString(R.string.base)

    override fun getList() =
        buildRecyclerList(17) {
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(2) }
                }
                unit = "₂"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(3) }
                }
                unit = "₃"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(4) }
                }
                unit = "₄"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(5) }
                }
                unit = "₅"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(6) }
                }
                unit = "₆"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(7) }
                }
                unit = "₇"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(8) }
                }
                unit = "₈"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(9) }
                }
                unit = "₉"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(10) }
                }
                unit = "₁₀"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(11) }
                }
                unit = "₁₁"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(12) }
                }
                unit = "₁₂"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(13) }
                }
                unit = "₁₃"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(14) }
                }
                unit = "₁₄"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(15) }
                }
                unit = "₁₅"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(16) }
                }
                unit = "₁₆"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(32) }
                }
                unit = "₃₂"
            }
            putEntry {
                quantity = appendString {
                    addWithSpace { base }
                    add { decimalFormat.format(36) }
                }
                unit = "₃₆"
            }
        }
}