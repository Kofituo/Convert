package com.example.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.unitconverter.AdditionItems.mProgress

class MyMotionLayout(context: Context, attributeSet: AttributeSet? = null) :
    MotionLayout(context,attributeSet) {

    init {
        setTransitionListener(object : TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                mProgress = p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

            }
        })
    }
}
