package com.otuolabs.unitconverter.recyclerViewData

import android.content.Context
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.miscellaneous.appendString
import com.otuolabs.unitconverter.miscellaneous.value
import com.otuolabs.unitconverter.miscellaneous.valueWithSpace

class Velocity(override val context: Context) : RecyclerDataAbstractClass() {

    private val radians = getString(R.string.radians)
    private val radiansUnit = getString(R.string.radians_unit)
    private val seconds = getString(R.string.seconds).toLowerCase(locale)
    private val secondsUnit = getString(R.string.seconds_unit)
    private val degrees = getString(R.string.degrees)
    private val degreesUnit = getString(R.string.degrees_unit)
    private val per = getString(R.string.per)
    private val perUnit = getString(R.string.per_unit)
    private val hour = getString(R.string.hour).toLowerCase(locale)
    private val hourUnit = getString(R.string.hour_unit)
    private val minute = getString(R.string.minute).toLowerCase(locale)
    private val minuteUnit = getString(R.string.minute_unit)
    private val day = getString(R.string.day).toLowerCase(locale)
    private val grad = getString(R.string.grad)
    private val gradUnit = getString(R.string.grad_unit)
    private val rev = getString(R.string.revolution)
    private val revUnit = getString(R.string.revolution_unit)

    override fun getList() =
        buildRecyclerList(16) {
            putEntry {
                quantity = appendString {
                    this valueWithSpace radians
                    this valueWithSpace per
                    this value seconds
                }
                unit = appendString {
                    this value radiansUnit
                    this value perUnit
                    this value secondsUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace radians
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value radiansUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace radians
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value radiansUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace radians
                    this valueWithSpace per
                    this value day
                }
                unit = appendString {
                    this value radiansUnit
                    this value perUnit
                    this value day
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace degrees
                    this valueWithSpace per
                    this value seconds
                }
                unit = appendString {
                    this value degreesUnit
                    this value perUnit
                    this value secondsUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace degrees
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value degreesUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace degrees
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value degreesUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace degrees
                    this valueWithSpace per
                    this value day
                }
                unit = appendString {
                    this value degreesUnit
                    this value perUnit
                    this value day
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace grad
                    this valueWithSpace per
                    this value seconds
                }
                unit = appendString {
                    this value gradUnit
                    this value perUnit
                    this value secondsUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace grad
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value gradUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace grad
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value gradUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace grad
                    this valueWithSpace per
                    this value day
                }
                unit = appendString {
                    this value gradUnit
                    this value perUnit
                    this value day
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace rev
                    this valueWithSpace per
                    this value seconds
                }
                unit = appendString {
                    this value revUnit
                    this value perUnit
                    this value secondsUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace rev
                    this valueWithSpace per
                    this value minute
                }
                unit = appendString {
                    this value revUnit
                    this value perUnit
                    this value minuteUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace rev
                    this valueWithSpace per
                    this value hour
                }
                unit = appendString {
                    this value revUnit
                    this value perUnit
                    this value hourUnit
                }
            }
            putEntry {
                quantity = appendString {
                    this valueWithSpace rev
                    this valueWithSpace per
                    this value day
                }
                unit = appendString {
                    this value revUnit
                    this value perUnit
                    this value day
                }
            }
        }
}