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
                CharArray(1).contentToString()
                unit
            }
        }
}