package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.appendString
import com.example.unitconverter.miscellaneous.value

class Sound(override val context: Context) : RecyclerDataAbstractClass() {

    private val _bel = getString(R.string.bel)
    private val bel = _bel.toLowerCase(locale)
    private val belUnit = getString(R.string.bel_unit)

    override fun getList() = buildRecyclerList(4) {
        putEntry {
            quantity = appendString(3) {
                this value _bel
            }
            unit = appendString(1) {
                this value belUnit
            }
        }
        putEntry {
            quantity = appendString(7) {
                this value milli
                this value bel
            }
            unit = appendString(2) {
                this value milliSymbol
                this value belUnit
            }
        }
        putEntry {
            quantity = appendString(7) {
                this value deci
                this value bel
            }
            unit = appendString(2) {
                this value deciSymbol
                this value belUnit
            }
        }
        putEntry {
            quantity = appendString(5) {
                this value getString(R.string.neper)
            }
            unit = appendString(2) {
                this value getString(R.string.neper_unit)
            }
        }
    }
}