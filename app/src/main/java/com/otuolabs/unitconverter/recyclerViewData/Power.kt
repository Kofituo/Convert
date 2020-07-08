package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.builders.addAll
import com.otuolabs.unitconverter.miscellaneous.appendString
import com.otuolabs.unitconverter.miscellaneous.value
import com.otuolabs.unitconverter.miscellaneous.valueWithSpace

class Power(override val context: Context) : RecyclerDataAbstractClass() {

    private val _watt = getString(R.string.watt)
    private val watt = _watt.toLC()
    private val wattUnit = getString(R.string.watt_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val hour = getString(R.string.hour).toLC()
    private val hourUnit = getString(R.string.hour_unit)
    private val second = getString(R.string.seconds).toLC()
    private val secondUnit = getString(R.string.seconds_unit)
    private val minute = getString(R.string.minute).toLC()
    private val minuteUnit = getString(R.string.minute_unit)
    private val footPound = getString(R.string.foot_pound)
    private val footPoundUnit = getString(R.string.foot_pound_unit)

    override fun getList() =
        buildRecyclerList(35) {
            putEntry {
                quantity = _watt
                unit = wattUnit
            }
            addAll {
                massPrefixes(watt, wattUnit)
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace getString(R.string.erg)
                    this valueWithSpace per
                    this value second
                }
                unit = appendString(5) {
                    this value getString(R.string.erg_unit)
                    this value perUnit
                    this value secondUnit
                }
            }
            getString(R.string.joule) {
                val jouleUnit = getString(R.string.joule_unit)
                putEntry {
                    quantity = appendString {
                        this valueWithSpace this@getString
                        this valueWithSpace per
                        this value minute
                    }
                    unit = appendString {
                        this value jouleUnit
                        this value perUnit
                        this value minuteUnit
                    }
                }
                putEntry {
                    quantity = appendString {
                        this valueWithSpace this@getString
                        this valueWithSpace per
                        this value hour
                    }
                    unit = appendString {
                        this value jouleUnit
                        this value perUnit
                        this value hourUnit
                    }
                }
            }
            getString(R.string.horsepower_unit) {
                putEntry {
                    quantity = getString(R.string.metric_horsepower)
                    unit = this@getString
                }
                putEntry {
                    quantity = getString(R.string.imp_horsepower)
                    unit = this@getString
                }
                putEntry {
                    quantity = getString(R.string.electric_horsepower)
                    unit = this@getString
                }
            }
            val calories = getString(R.string.large_calorie)
            val caloriesUnit = getString(R.string.calorie_unit)

            putEntry {
                quantity = appendString {
                    this valueWithSpace calories
                    this valueWithSpace per
                    this value second
                }
                unit = appendString {
                    this value caloriesUnit
                    this value perUnit
                    this value secondUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace calories
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value caloriesUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace calories
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value caloriesUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            calories.toLC().let {
                putEntry {
                    quantity = appendString {
                        this value kilo
                        this valueWithSpace it
                        this valueWithSpace per
                        this value second
                    }
                    unit = appendString {
                        this value kiloSymbol
                        this value caloriesUnit
                        this value perUnit
                        this value secondUnit
                    }
                }
                putEntry {
                    quantity = appendString {
                        this value kilo
                        this valueWithSpace it
                        this valueWithSpace per
                        this value minute
                    }
                    unit = appendString {
                        this value kiloSymbol
                        this value caloriesUnit
                        this value perUnit
                        this value minuteUnit
                    }
                }
                putEntry {
                    quantity = appendString {
                        this value kilo
                        this valueWithSpace it
                        this valueWithSpace per
                        this value hour
                    }
                    unit = appendString {
                        this value kiloSymbol
                        this value caloriesUnit
                        this value perUnit
                        this value hourUnit
                    }
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace footPound
                    this valueWithSpace per
                    this value second
                }
                unit = appendString {
                    this value footPoundUnit
                    this value perUnit
                    this value secondUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace footPound
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value footPoundUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace footPound
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value footPoundUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            getString(R.string.british_thermal_unit) {
                val btu = getString(R.string.btu)
                putEntry {
                    quantity = appendString {
                        this valueWithSpace this@getString
                        this valueWithSpace per
                        this value second
                    }
                    unit = appendString {
                        this value btu
                        this value perUnit
                        this value secondUnit
                    }
                }
                putEntry {
                    quantity = appendString {
                        this valueWithSpace this@getString
                        this valueWithSpace per
                        this value minute
                    }
                    unit = appendString {
                        this value btu
                        this value perUnit
                        this value minuteUnit
                    }
                }
                putEntry {
                    quantity = appendString {
                        this valueWithSpace this@getString
                        this valueWithSpace per
                        this value hour
                    }
                    unit = appendString {
                        this value btu
                        this value perUnit
                        this value hourUnit
                    }
                }
            }
        }
}