package com.example.unitconverter.recyclerViewData

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import com.example.unitconverter.R

class Angle(override val context: Context) : RecyclerDataAbstractClass() {

    override fun getList() = buildRecyclerList(9) {
        add(R.string.degrees, R.string.degrees_unit)
        add(R.string.radians, R.string.radians_unit)
        add(R.string.grad, R.string.grad_unit)
        add(R.string.revolution, R.string.revolution_unit)
        add( //bold programmatically
            getString(R.string.arc_minute),
            SpannableString(getString(R.string.arc_minute_unit)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
            })
        add(
            getString(R.string.arc_second),
            SpannableString(getString(R.string.arc_second_unit)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
            }
        )
        add(R.string.quadrant, R.string.quadrant_unit)
        add(R.string.sextant, R.string.sextant_unit)
        add(R.string.octant, R.string.octant_units)
    }
}