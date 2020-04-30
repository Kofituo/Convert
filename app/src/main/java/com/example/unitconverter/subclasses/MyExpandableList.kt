package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListView

class MyExpandableList(context: Context, attributeSet: AttributeSet?) :
    ExpandableListView(context, attributeSet) {

    var maxHeight: Int? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            maxHeight?.let { MeasureSpec.makeMeasureSpec(it, MeasureSpec.AT_MOST) }
                ?: heightMeasureSpec)
    }
}