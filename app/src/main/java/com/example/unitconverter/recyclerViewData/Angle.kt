package com.example.unitconverter.recyclerViewData

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass

class Angle(override val context: Context) : RecyclerDataInterface {

    override fun getList(): MutableList<RecyclerDataClass> {
        return mutableListOf<RecyclerDataClass>().apply {
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
        }
    }

    override var start = 0
}