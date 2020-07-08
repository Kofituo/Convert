package com.otuolabs.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.otuolabs.unitconverter.R

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
