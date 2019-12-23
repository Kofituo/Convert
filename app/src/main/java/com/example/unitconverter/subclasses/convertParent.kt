package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class ConvertMotionLayout(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("event", "${event?.actionMasked}")
        return super.onTouchEvent(event)
    }
}