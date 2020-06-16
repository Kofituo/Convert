package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.builders.addAll
import com.example.unitconverter.miscellaneous.appendString
import com.example.unitconverter.miscellaneous.value

class Force(override val context: Context) : RecyclerDataAbstractClass() {

    private val _newton = getString(R.string.newton_temperature)
    private val newton = _newton.toLC()
    private val newtonUnit = getString(R.string.newton_unit)
    private val gramForce = getString(R.string.gram_force)
    private val gramForceUnit = getString(R.string.gram_force_unit)
    private val tonForceUnit = getString(R.string.ton_force_unit)

    override fun getList() = buildRecyclerList(0) {
        putEntry {
            quantity = _newton
            unit = newtonUnit
        }
        addAll {
            massPrefixes(newton, newtonUnit)
        }
        putEntry {
            quantity = getString(R.string.dyne)
            unit = getString(R.string.dyne_unit)
        }
        putEntry {
            quantity = gramForce
            unit = gramForceUnit
        }
        putEntry {
            quantity = appendString {
                this value kilo
                this value gramForce
            }
            unit = appendString(3) {
                this value kiloSymbol
                this value gramForceUnit
            }
        }
        putEntry {
            quantity = getString(R.string.poundal)
            unit = getString(R.string.poundal_unit)
        }
        putEntry {
            quantity = getString(R.string.pound_force)
            unit = getString(R.string.pound_force_unit)
        }
        putEntry {
            getString(R.string.kip) {
                quantity = this
                unit = this
            }
        }
        putEntry {
            quantity = getString(R.string.s_ton_force)
            unit = tonForceUnit
        }
        putEntry {
            quantity = getString(R.string.l_ton_force)
            unit = tonForceUnit
        }
    }
}