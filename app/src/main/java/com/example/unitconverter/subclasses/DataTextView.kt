package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import com.example.unitconverter.R
import com.google.android.material.textview.MaterialTextView

class DataTextView(context: Context, attributeSet: AttributeSet?) :
    MaterialTextView(context, attributeSet) {

    var metadata: String? = null

    init {
        context
            .theme
            .obtainStyledAttributes(attributeSet, R.styleable.DataTextView, 0, 0)
            .apply {
                metadata = getString(R.styleable.DataTextView_metadata)
                recycle()
            }
    }
}
