package com.otuolabs.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class ConvertScrollView(context: Context, attributeSet: AttributeSet) :
    NestedScrollView(context, attributeSet) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}