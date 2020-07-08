package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.put
import com.otuolabs.unitconverter.miscellaneous.value
import com.otuolabs.unitconverter.miscellaneous.valueWithSpace

class Energy(override val context: Context) : RecyclerDataAbstractClass() {
    private val _joule = getString(R.string.joule)
    private val joule = _joule.toLowerCase(locale)
    private val jouleUnit = getString(R.string.joule_unit)
    private val tnt = getString(R.string.tnt)
    private val _gram = getString(R.string.gram)
    private val gram = _gram.toLowerCase(locale)
    private val gramUnit = getString(R.string.gram_unit)
    private val of = getString(R.string.of)
    private val _ton = getString(R.string.ton)
    private val ton = _ton.toLowerCase(locale)
    private val tonUnit = getString(R.string.metricTonUnit)
    private val calorie = getString(R.string.small_calorie)
    private val calorieUnit = getString(R.string.calorie_unit)
    private val _wattHour = getString(R.string.watt_hour)
    private val wattHour = _wattHour.toLowerCase(locale)
    private val wattHourUnit = getString(R.string.watt_hour_unit)

    override fun getList() =
        buildRecyclerList(34) {
            add(_joule, jouleUnit)
            this putAll massPrefixes(joule, jouleUnit)
            putEntry {
                this quantity getString(R.string.erg)
                this unit getString(R.string.erg_unit)
            }
            add(calorie, calorieUnit)
            putEntry {
                this quantity buildString {
                    put {
                        this valueWithSpace kilo
                        this valueWithSpace getString(R.string.food)
                        this value getString(R.string.large_calorie)
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value calorieUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this valueWithSpace _gram
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit gramUnit
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value kilo
                        this valueWithSpace gram
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value gramUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this valueWithSpace _ton
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit tonUnit
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value kilo
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value tonUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value mega
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value megaSymbol
                        this value tonUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value giga
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value gigaSymbol
                        this value tonUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value tera
                        this valueWithSpace ton
                        this valueWithSpace of
                        this value tnt
                    }
                }
                this unit buildString {
                    put {
                        this value teraSymbol
                        this value tonUnit
                    }
                }
            }
            putEntry {
                this quantity getString(R.string.foot_pound_force)
                this unit getString(R.string.foot_pound_unit)
            }
            putEntry {
                this quantity getString(R.string.electron_volt)
                this unit getString(R.string.electron_volt_unit)
            }
            putEntry {
                this quantity getString(R.string.british_thermal_unit)
                this unit getString(R.string.british_thermal_unit_unt)
            }
            putEntry {
                this quantity _wattHour
                this unit wattHourUnit
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value milli
                        this value wattHour
                    }
                }
                this unit buildString {
                    put {
                        this value milliSymbol
                        this value wattHourUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value kilo
                        this value wattHour
                    }
                }
                this unit buildString {
                    put {
                        this value kiloSymbol
                        this value wattHourUnit
                    }
                }
            }
            putEntry {
                this quantity buildString {
                    put {
                        this value mega
                        this value wattHour
                    }
                }
                this unit buildString {
                    put {
                        this value megaSymbol
                        this value wattHourUnit
                    }
                }
            }
        }
}