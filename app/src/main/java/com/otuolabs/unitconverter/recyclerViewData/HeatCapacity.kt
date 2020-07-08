package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.appendString
import com.otuolabs.unitconverter.miscellaneous.value
import com.otuolabs.unitconverter.miscellaneous.valueWithSpace

class HeatCapacity(override val context: Context) : RecyclerDataAbstractClass() {

    private val _joule = getString(R.string.joule)
    private val joule = _joule.toLowerCase(locale)
    private val jouleUnit = getString(R.string.joule_unit)
    private val kelvin = getString(R.string.kelvin)
    private val kelvinUnit = getString(R.string.kelvin_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val _calorie = getString(R.string.large_calorie)
    private val calorieUnit = getString(R.string.calorie_unit)
    private val btu = getString(R.string.british_thermal_unit)
    private val btuUnit =
        getString(R.string.british_thermal_unit_unt)
            .substringBefore(' ') //get only btu

    override fun getList() =
        buildRecyclerList(8) {
            putEntry {
                this quantity appendString {
                    this valueWithSpace _joule
                    this valueWithSpace per
                    this value kelvin
                }
                this unit appendString {
                    this value jouleUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                this quantity appendString {
                    this value kilo
                    this valueWithSpace joule
                    this valueWithSpace per
                    this value kelvin
                }
                this unit appendString {
                    this value kiloSymbol
                    this value jouleUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                this quantity appendString {
                    this value mega
                    this valueWithSpace joule
                    this valueWithSpace per
                    this value kelvin
                }
                this unit appendString {
                    this value megaSymbol
                    this value jouleUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                this quantity appendString {
                    this value giga
                    this valueWithSpace joule
                    this valueWithSpace per
                    this value kelvin
                }
                this unit appendString {
                    this value megaSymbol
                    this value jouleUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                this quantity appendString {
                    this valueWithSpace _calorie
                    this valueWithSpace per
                    this value kelvin
                }
                this unit appendString {
                    this value calorieUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this value kilo
                    this valueWithSpace _calorie.toLowerCase(locale)
                    this valueWithSpace per
                    this value kelvin
                }
                unit = appendString {
                    this value kiloSymbol
                    this value calorieUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace btu
                    this valueWithSpace per
                    this value getString(R.string.fahrenheit)
                }
                unit = appendString {
                    this value btuUnit
                    this value perUnit
                    this value getString(R.string.fahrenheit_unit)
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace btu
                    this valueWithSpace per
                    this value kelvin
                }
                unit = appendString {
                    this value btuUnit
                    this value perUnit
                    this value kelvinUnit
                }
            }
        }
}