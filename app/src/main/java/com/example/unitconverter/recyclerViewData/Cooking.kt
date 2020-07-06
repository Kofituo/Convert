package com.example.unitconverter.recyclerViewData

import android.content.Context
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.add
import com.example.unitconverter.miscellaneous.appendString

class Cooking(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() =
        buildRecyclerList(10) {
            putEntry {
                quantity = getString(R.string.pinch)
                unit = getString(R.string.pinch_unit)
            }
            putEntry {
                quantity = getString(R.string.drop)
                unit = getString(R.string.drop_unit)
            }
            putEntry {
                quantity = getString(R.string.coffee_spoon)
                unit = getString(R.string.coffee_unit)
            }
            putEntry {
                quantity = getString(R.string.teaspoon)
                unit = getString(R.string.teaspoon_unit)
            }
            putEntry {
                quantity = getString(R.string.tablespoon)
                unit = getString(R.string.tablespoon_unit)
            }
            putEntry {
                quantity = getString(R.string.dessertspoon)
                unit = getString(R.string.dessertspoon_unit)
            }
            putEntry {
                quantity = getString(R.string.Fluid_ounce)
                unit = getString(R.string.fluid_ounce)
            }
            putEntry {
                quantity = appendString {
                    add { milli }
                    add { getString(R.string.litre).toLC() }
                }
                unit = appendString {
                    add { milliSymbol }
                    add { getString(R.string.litre_unit) }
                }
            }
            putEntry {
                quantity = getString(R.string.wineglass)
                unit = getString(R.string.wineglass_unit)
            }
            putEntry {
                quantity = getString(R.string.cup)
                unit = getString(R.string.cup_unit)
            }
        }
}